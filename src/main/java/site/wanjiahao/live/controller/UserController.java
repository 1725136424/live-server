package site.wanjiahao.live.controller;

import java.util.Arrays;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.tomcat.util.bcel.Const;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import site.wanjiahao.live.constant.Constant;
import site.wanjiahao.live.entity.UserEntity;
import site.wanjiahao.live.entity.UserEntityVo;
import site.wanjiahao.live.enumerate.StatusCodeEnum;
import site.wanjiahao.live.enumerate.UserStatus;
import site.wanjiahao.live.exception.*;
import site.wanjiahao.live.service.SMSService;
import site.wanjiahao.live.service.UserService;
import site.wanjiahao.live.utils.CommonUtils;
import site.wanjiahao.live.utils.CookieUtils;
import site.wanjiahao.live.utils.PageUtils;
import site.wanjiahao.live.utils.R;
import site.wanjiahao.live.valid.group.SaveGroup;
import site.wanjiahao.live.valid.group.UpdateGroup;
import site.wanjiahao.live.vo.AnchorVo;
import site.wanjiahao.live.vo.LoginUserVo;
import site.wanjiahao.live.vo.ResetVo;

import javax.servlet.http.HttpServletResponse;

/**
 * @author haodada
 * @email 1725136424@qq.com
 * @date 2020-12-26 16:39:16
 */
@RestController
@RequestMapping("live/user")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private SMSService smsService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    // @RequiresPermissions("live:user:list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = userService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    // @RequiresPermissions("live:user:info")
    public R info(@PathVariable("id") String id) {
        UserEntity user = userService.findById(id);

        return R.ok().put("user", user);
    }

    /**
     * 查询当前手机号是否已经注册
     */
    @GetMapping("/isExist/{phone}")
    public R isExist(@PathVariable("phone") String phone) {
        try {
            boolean isExist = userService.findExistByPhone(phone);
            return R.ok().put("isExist", isExist);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("查询用户是否已存接口异常{}", e.getMessage());
            return R.error().put("isExist", true);
        }
    }

    /**
     * 判断是否已经登录
     */
    @GetMapping("/isLogin")
    public R isLogin(@CookieValue(value = "token", required = false) String token) {
        try {
            // 获取用户
            String userStr = stringRedisTemplate.opsForValue().get(Constant.USER_PREFIX + ":" + token);
            if (!StringUtils.isBlank(userStr)) {
                UserEntity userEntity = JSON.parseObject(userStr, UserEntity.class);
                UserEntityVo userEntityVo = new UserEntityVo();
                // 剔除敏感数据
                BeanUtils.copyProperties(userEntity, userEntityVo);
                userEntityVo.setPassword(null);
                return R.ok().put("user", userEntityVo);
            } else {
                return R.ok().put("user", null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("验证是否登录接口错误{}", e.getMessage());
            return R.error();
        }
    }

    /**
     * 退出登录
     */
    @GetMapping("/logout")
    public R logout(@CookieValue("token") String token) {
        try {
            stringRedisTemplate.delete(Constant.USER_PREFIX + ":" + token);
            return R.ok();
        } catch (Exception e) {
            e.printStackTrace();
            log.error("退出登录接口异常{}", e.getMessage());
            return R.error();
        }
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    // @RequiresPermissions("live:user:save")
    public R save(@Validated(SaveGroup.class) @RequestBody UserEntityVo user) {
        // 判断当前用户是否存在
        try {
            boolean existByPhone = userService.findExistByPhone(user.getPhone());
            if (existByPhone) {
                return R.error(StatusCodeEnum.PHONE_IS_EXIST.getCode(), StatusCodeEnum.PHONE_IS_EXIST.getMsg());
            }
            // 验证码校验
            boolean isPass = smsService.verifyCode(user.getCode(), user.getPhone());
            if (isPass) {
                UserEntity userEntity = new UserEntity();
                BeanUtils.copyProperties(user, userEntity);
                // 设置默认状态
                userEntity.setStatus(UserStatus.NORMAL.getCode());
                // 密码加密处理
                String password = CommonUtils.MD5Hex(user.getPassword(), Constant.SALT, Constant.num);
                userEntity.setPassword(password);
                userService.save(userEntity);
                return R.ok();
            }
            return R.error(StatusCodeEnum.RANDOM_NOT_MATCH.getCode(), StatusCodeEnum.RANDOM_NOT_MATCH.getMsg());
        } catch (Exception e) {
            e.printStackTrace();
            log.error("保存用户接口出现错误{}", e.getMessage());
            return R.error();
        }

    }

    /**
     * 重置密码
     */
    @PostMapping("/reset")
    public R reset(@Validated @RequestBody ResetVo resetVo) {
        try {
            userService.resetPassword(resetVo);
            return R.ok();
        } catch (RandomCodeException | PhoneNotExistException e) {
            String name = e.getClass().getSimpleName();
            if ("RandomCodeException".equals(name)) {
                return R.error(StatusCodeEnum.RANDOM_NOT_MATCH.getCode(), StatusCodeEnum.RANDOM_NOT_MATCH.getMsg());
            } else if ("PhoneNotExistException".equals(name)) {
                return R.error(StatusCodeEnum.PHONE_NOT_EXIST.getCode(), StatusCodeEnum.PHONE_NOT_EXIST.getMsg());
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("重置密码接口出现错误{}", e.getMessage());
        }
        return R.error();
    }

    /**
     * 注册主播
     */
    //
    @PostMapping("/anchor")
    public R anchor(@Validated @RequestBody AnchorVo anchorVo) {
        try {
            userService.anchor(anchorVo);
            return R.ok();
        } catch (MultAnchorException e) {
            return R.error(StatusCodeEnum.ANCHOR_MULT_EXCEPTION.getCode(),
                    StatusCodeEnum.ANCHOR_MULT_EXCEPTION.getMsg());
        } catch (Exception e) {
            e.printStackTrace();
            log.error("注册主播接口异常{}", e.getMessage());
            return R.error();
        }
    }

    /**
     * 审核主播
     */
    @GetMapping("/passAnchor/{id}")
    public R passAnchor(@PathVariable("id") String id) {
        try {
            userService.passAnchor(id);
            return R.ok();
        } catch (NoPermissionException e) {
            return R.error(StatusCodeEnum.NO_PERMISSION_EXCEPTION.getCode(),
                    StatusCodeEnum.NO_PERMISSION_EXCEPTION.getMsg());
        } catch (UserNoExistException e) {
            return R.error(StatusCodeEnum.USER_NOT_EXIST_EXCEPTION.getCode(),
                    StatusCodeEnum.USER_NOT_EXIST_EXCEPTION.getMsg());
        } catch (StatusDisaccordException e) {
            return R.error(StatusCodeEnum.STATUS_DISACCORD_EXCEPTION.getCode(),
                    StatusCodeEnum.STATUS_DISACCORD_EXCEPTION.getMsg());
        } catch (Exception e) {
            e.printStackTrace();
            log.error("主播审核接口出现异常{}", e.getMessage());
            return R.error();
        }
    }

    /**
     * 登录
     */
    @PostMapping("/login")
    public R login(@Validated @RequestBody LoginUserVo loginUserVo,
                   @CookieValue(value = "token", required = false) String token,
                   HttpServletResponse response) {
        try {
            // 判断是否已经登录
            String redisUser = stringRedisTemplate.opsForValue().get(Constant.USER_PREFIX + ":" + token);
            if (!StringUtils.isBlank(redisUser)) {
                return R.error(StatusCodeEnum.LOGINED.getCode(), StatusCodeEnum.LOGINED.getMsg());
            }
            // 密码加密
            String hexPassword = CommonUtils.MD5Hex(loginUserVo.getPassword(), Constant.SALT, Constant.num);
            loginUserVo.setPassword(hexPassword);
            // 判断是否登录
            UserEntity userEntity = userService.login(loginUserVo);
            if (userEntity != null) {
                // 保存用户至redis中
                String uuid = UUID.randomUUID().toString().replace("-", "");
                String userStr = JSON.toJSONString(userEntity);
                // 保存用户至redis中
                stringRedisTemplate.opsForValue().set(Constant.USER_PREFIX + ":" + uuid, userStr, Constant.REDIS_USER_EXPIRE, TimeUnit.SECONDS);
                // 响应cookie至浏览器中
                CookieUtils.setCookie(response, Constant.USER_TOKEN, uuid);
                return R.ok();
            }
            return R.error(StatusCodeEnum.AUTH_ERROR.getCode(),
                    StatusCodeEnum.AUTH_ERROR.getMsg());
        } catch (Exception e) {
            e.printStackTrace();
            log.error("登录接口出现错误{}", e.getMessage());
            return R.error();
        }
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    // @RequiresPermissions("live:user:update")
    public R update(@Validated(UpdateGroup.class) UserEntity user) {
        userService.diyUpdateById(user);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    // @RequiresPermissions("live:user:delete")
    public R delete(@RequestBody String[] ids) {
        userService.diyRemoveByIds(Arrays.asList(ids));

        return R.ok();
    }

}
