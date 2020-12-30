package site.wanjiahao.live.service.impl;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import site.wanjiahao.live.constant.Constant;
import site.wanjiahao.live.entity.UserEntity;
import site.wanjiahao.live.enumerate.LiveStatusEnum;
import site.wanjiahao.live.enumerate.UserStatus;
import site.wanjiahao.live.exception.NoAnchorException;
import site.wanjiahao.live.interceptor.LoginInterceptor;
import site.wanjiahao.live.service.UserLiveService;
import site.wanjiahao.live.service.UserService;
import site.wanjiahao.live.utils.PageUtils;
import site.wanjiahao.live.utils.Query;

import site.wanjiahao.live.dao.LiveDao;
import site.wanjiahao.live.entity.LiveEntity;
import site.wanjiahao.live.service.LiveService;
import site.wanjiahao.live.utils.live.AliLiveUtils;
import site.wanjiahao.live.vo.LiveHallVo;
import site.wanjiahao.live.vo.LiveVo;
import site.wanjiahao.live.vo.StartAnchorVo;
import site.wanjiahao.live.vo.StreamUrlVo;


@Service("liveService")
@Slf4j
@CacheConfig(cacheNames = Constant.LIVE_CACHE_PREFIX)
public class LiveServiceImpl extends ServiceImpl<LiveDao, LiveEntity> implements LiveService {

    @Autowired
    private AliLiveUtils aliLiveUtils;

    @Autowired
    private UserLiveService userLiveService;

    @Autowired
    private UserService userService;

    @Value("${alibaba.live.picSuffix}")
    private String picSuffix;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        // 获取当前正在推流的直播间
        QueryWrapper<LiveEntity> wrapper = new QueryWrapper<LiveEntity>().eq("status", LiveStatusEnum.PUBLISH.getCode());
        String keywords = (String) params.get("keywords");
        // 安装关键字查询
        if (!StringUtils.isBlank(keywords)) {
            wrapper.like("title", "%"+ keywords +"%");
        }
        // 查询某分类下
        String category = (String) params.get("category");
        if (!StringUtils.isBlank(category)) {
            wrapper.eq("cid", category);
        }
        IPage<LiveEntity> page = this.page(
                new Query<LiveEntity>().getPage(params),
                wrapper
        );
        List<LiveEntity> records = page.getRecords();
        // 获取当前的直播信息
        List<LiveHallVo> collect = records.stream().map((item) -> {
            String appName = item.getAppName();
            String streamName = item.getStreamName();
            // 计算热度
            Long hot = aliLiveUtils.sumHot(appName, streamName);
            // 获取实时图片
            String currentPic = picSuffix + appName + "/" + streamName + ".jpg";
            // 获取当前用户
            String idCard = item.getIdCard();
            UserEntity userEntity = userService.findByIdCard(idCard);
            LiveHallVo liveHallVo = new LiveHallVo();
            liveHallVo.setHot(hot);
            liveHallVo.setId(item.getId());
            liveHallVo.setLivePic(currentPic);
            liveHallVo.setPlayUrl(item.getPlayUrl());
            liveHallVo.setTitle(item.getTitle());
            liveHallVo.setUsername(userEntity.getUsername());
            liveHallVo.setUserPic(userEntity.getPic());
            return liveHallVo;
        }).collect(Collectors.toList());
        PageUtils pageUtils = new PageUtils(page);
        pageUtils.setList(collect);
        return pageUtils;
    }

    @Override
    public StreamUrlVo startAnchor(StartAnchorVo startAnchorVo) {
        // 获取当前线程的用户
        UserEntity userEntity = LoginInterceptor.threadLocal.get();
        // 当前从本地线程获取的数据，也就是从缓存中获取的，缓存中的数据只作为登录与否的标记，数据可能会与数据库的数据不一致
        String id = userEntity.getId();
        // 查询真实数据
        userEntity = userService.findById(id);
        // 判断当前用户是否为已经注册的主播
        Integer status = userEntity.getStatus();
        if (UserStatus.ANCHOR.getCode() == status) {
            // 获取当前用户的直播间信息
            String idCard = userEntity.getIdCard();
            LiveEntity liveEntity = this.findByIdCard(idCard);
            // 标题
            liveEntity.setTitle(startAnchorVo.getTitle());
            // 分类
            liveEntity.setCid(startAnchorVo.getCategory());
            // 构造推流地址。播流地址
            String pushUrl = aliLiveUtils.createPushUrl(liveEntity.getAppName(), liveEntity.getStreamName());
            List<String> playUrls = aliLiveUtils.createPlayUrl(liveEntity.getAppName(), liveEntity.getStreamName());
            String join = StrUtil.join(";", playUrls);
            liveEntity.setPushUrl(pushUrl);
            liveEntity.setPlayUrl(join);
            // 更新直播间信息
            this.diyUpdateById(liveEntity);
            // 返回流对象
            StreamUrlVo streamUrlVo = new StreamUrlVo();
            streamUrlVo.setPushUrl(pushUrl);
            streamUrlVo.setPlayUrl(join);
            return streamUrlVo;
        } else {
            // 直接抛出不是主播的异常
            throw new NoAnchorException("但是用户不是注册主播");
        }
    }

    @Cacheable(key = "'liveEntity-one' + #p0")
    @Override
    public LiveEntity findByIdCard(String id_card) {
        return baseMapper.selectOne(new QueryWrapper<LiveEntity>().eq("id_card", id_card));
    }

    @CacheEvict(allEntries = true)
    @Override
    public void diyUpdateById(LiveEntity liveEntity) {
        updateById(liveEntity);
    }

    @CacheEvict(allEntries = true)
    @Override
    public void diyRemoveByIds(List<String> asList) {
        removeByIds(asList);
    }

    @Cacheable(key = "'liveEntity-one' + #p0")
    @Override
    public LiveEntity findById(String id) {
        return baseMapper.selectById(id);
    }

    @Cacheable(key = "'roomIsExist-one' + #p0")
    @Override
    public boolean roomIsExist(String id) {
        return baseMapper.selectById(id) != null;
    }

    @Cacheable(key = "'liveEntity-one' + #p0 + '-' + #p1")
    @Override
    public LiveEntity findByAppNameAndStreamName(String appName, String id) {
        return baseMapper
                .selectOne(new QueryWrapper<LiveEntity>()
                        .eq("app_name", appName).eq("stream_name", id));
    }

    @Override
    public LiveVo findLiveVoById(String id) {
        LiveEntity liveEntity = this.findById(id);
        // 判断当前直播间是否存在
        if (liveEntity != null) {
            // 查询当前用户
            String idCard = liveEntity.getIdCard();
            UserEntity userEntity = userService.findByIdCard(idCard);
            // 查询热度
            Long hot = aliLiveUtils.sumHot(liveEntity.getAppName(), liveEntity.getStreamName());
            aliLiveUtils.sumOnlineNumbers(liveEntity.getAppName(), liveEntity.getStreamName());
            LiveVo liveVo = new LiveVo();
            liveVo.setHot(hot);
            liveVo.setPlayUrl(liveEntity.getPlayUrl());
            liveVo.setTitle(liveEntity.getTitle());
            liveVo.setUserPic(userEntity.getPic());
            liveVo.setUsername(userEntity.getUsername());
            // 获取当前直播间的关注用户集合
            Set<String> ids = userLiveService.listUserIdByLiveId(id);
            liveVo.setIds(ids);
            return liveVo;
        } else {
            return null;
        }
    }

    @Cacheable(key = "'liveEntity-one' + #p0")
    @Override
    public LiveEntity diyGetById(String id) {
        return getById(id);
    }

    @CacheEvict(allEntries = true)
    @Override
    public void diySave(LiveEntity live) {
        save(live);
    }

}