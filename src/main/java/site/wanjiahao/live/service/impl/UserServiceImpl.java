package site.wanjiahao.live.service.impl;

import cn.hutool.core.util.RandomUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;
import site.wanjiahao.live.constant.Constant;
import site.wanjiahao.live.entity.LiveEntity;
import site.wanjiahao.live.enumerate.LiveStatusEnum;
import site.wanjiahao.live.enumerate.TranscodeEnum;
import site.wanjiahao.live.enumerate.UserStatus;
import site.wanjiahao.live.exception.*;
import site.wanjiahao.live.interceptor.LoginInterceptor;
import site.wanjiahao.live.service.LiveService;
import site.wanjiahao.live.service.SMSService;
import site.wanjiahao.live.utils.CommonUtils;
import site.wanjiahao.live.utils.PageUtils;
import site.wanjiahao.live.utils.Query;
import site.wanjiahao.live.dao.UserDao;
import site.wanjiahao.live.entity.UserEntity;
import site.wanjiahao.live.service.UserService;
import site.wanjiahao.live.utils.live.AliLiveUtils;
import site.wanjiahao.live.vo.AnchorVo;
import site.wanjiahao.live.vo.LoginUserVo;
import site.wanjiahao.live.vo.ResetVo;
import sun.rmi.runtime.Log;

@Service("userService")
@CacheConfig(cacheNames = Constant.USER_CACHE_PREFIX)
public class UserServiceImpl extends ServiceImpl<UserDao, UserEntity> implements UserService {

    public static final UserEntity USER_ENTITY = LoginInterceptor.threadLocal.get();
    @Autowired
    private AliLiveUtils aliLiveUtils;

    @Autowired
    private LiveService liveService;

    @Autowired
    private UserService userService;

    @Autowired
    private SMSService smsService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<UserEntity> page = this.page(
                new Query<UserEntity>().getPage(params),
                new QueryWrapper<UserEntity>()
        );

        return new PageUtils(page);
    }

    @Cacheable(key = "'findExistByPhone-one' + #p0")
    @Override
    public boolean findExistByPhone(String phone) {
        return baseMapper.selectOne(new QueryWrapper<UserEntity>().eq("phone", phone)) != null;
    }

    @Override
    public UserEntity login(LoginUserVo loginUserVo) {
        return this.findByPhoneAndPassword(loginUserVo.getPhone(), loginUserVo.getPassword());
    }

    @Override
    public void anchor(AnchorVo anchorVo) {
        // TODO 验证身份证号码是否和真实姓名对应 调用对应的接口
        UserEntity USER_ENTITY = LoginInterceptor.threadLocal.get();
        // 再次查询，防止数据不一致问题
        UserEntity result = this.findById(USER_ENTITY.getId());
        if (UserStatus.VERIFY.getCode() == result.getStatus() || UserStatus.ANCHOR.getCode() == result.getStatus()) {
            throw new MultAnchorException("多次提交主播注册，或者已经注册异常");
        }
        // 设置真实姓名
        result.setName(anchorVo.getName());
        // 设置身份证号
        result.setIdCard(anchorVo.getIdCard());
        // 修改状态
        result.setStatus(UserStatus.VERIFY.getCode());
        // 保存用户
        this.diyUpdateById(result);
    }

    /**
     * 此方法必须是一个事务
     */
    @Override
    @Transactional
    public void passAnchor(String id) {
        // 获取本地线程用户
        UserEntity redisUser = LoginInterceptor.threadLocal.get();
        if (UserStatus.ADMIN.getCode() == redisUser.getStatus()) {
            // 查询当前用户
            UserEntity userEntity = this.findById(id);
            if (userEntity != null) {
                Integer status = userEntity.getStatus();
                if (UserStatus.VERIFY.getCode() == status) {
                    // 修改用户状态
                    userEntity.setStatus(UserStatus.ANCHOR.getCode());
                    // 设置直播间的基本信息
                    LiveEntity liveEntity = new LiveEntity();
                    // 随机分配直播间id
                    String roomId = "";
                    while (true) {
                        roomId = RandomUtil.randomNumbers(8);
                        boolean isExist = liveService.roomIsExist(roomId);
                        if (!isExist) {
                            break;
                        }
                    }
                    // 房间号
                    liveEntity.setId(roomId);
                    // 身份证信息
                    liveEntity.setIdCard(userEntity.getIdCard());
                    // appName
                    liveEntity.setAppName(roomId);
                    // streamName UUID
                    liveEntity.setStreamName(UUID.randomUUID().toString().replace("-", ""));
                    // 状态
                    liveEntity.setStatus(LiveStatusEnum.PUBLISH_DONE.getCode());
                    // 构造转码模板
                    TranscodeEnum[] values = TranscodeEnum.values();
                    for (TranscodeEnum value : values) {
                        // 排除普通转码
                        if ("normal".equals(value.getName())) {
                            continue;
                        }
                        // 构造4种转码模板
                        aliLiveUtils.buildTranscode(roomId, value);
                    }
                    // 修改用户状态
                    this.diyUpdateById(userEntity);
                    // 保存直播间初始信息
                    liveService.diySave(liveEntity);
                } else {
                    throw new StatusDisaccordException("状态不一致");
                }
            } else {
                throw new UserNoExistException("不存在此用户");
            }
        } else {
            throw new NoPermissionException("无权限操作");
        }
    }

    @Cacheable(key = "'userEntity-one' + #p0")
    @Override
    public UserEntity findByIdCard(String idCard) {
        return baseMapper.selectOne(new QueryWrapper<UserEntity>().eq("id_card", idCard));
    }

    @Cacheable(key = "'userEntity-one' + #p0")
    @Override
    public UserEntity findById(String id) {
        return baseMapper.selectById(id);
    }

    @Override
    public void resetPassword(ResetVo resetVo) {
        // 判断当前用户是否存在
       String phone = resetVo.getPhone();
        boolean exist = userService.findExistByPhone(phone);
        if (exist) {
            // 校验验证码
            boolean isPass = smsService.verifyCode(resetVo.getCode(), resetVo.getPhone());
            if (isPass) {
                // 密码加密
                String password = resetVo.getPassword();
                password = CommonUtils.MD5Hex(password, Constant.SALT, Constant.num);
                // 查询当前用户
                UserEntity userEntity = userService.findByPhone(phone);
                userEntity.setPassword(password);
                // 更新用户
                userService.diyUpdateById(userEntity);
            } else {
                throw new RandomCodeException("验证码不匹配");
            }
        } else {
            throw new PhoneNotExistException("当前手机号不存在");
        }
    }

    @Cacheable(key = "'userEntity-one' + #p0")
    @Override
    public UserEntity findByPhone(String phone) {
        return baseMapper.selectOne(new QueryWrapper<UserEntity>().eq("phone", phone));
    }

    @CacheEvict(allEntries = true)
    @Override
    public void diyUpdateById(UserEntity userEntity) {
        updateById(userEntity);
    }

    @CacheEvict(allEntries = true)
    @Override
    public void diyRemoveByIds(List<String> asList) {
        removeByIds(asList);
    }

    @Cacheable(key = "'userEntity-one' + #p0 + '-' + #p1")
    @Override
    public UserEntity findByPhoneAndPassword(String phone, String password) {
        return baseMapper.selectOne(new QueryWrapper<UserEntity>().eq("phone", phone).eq("password", password));
    }


}