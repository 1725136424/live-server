package site.wanjiahao.live.service.impl;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import site.wanjiahao.live.constant.Constant;
import site.wanjiahao.live.entity.UserEntity;
import site.wanjiahao.live.exception.AuthException;
import site.wanjiahao.live.interceptor.LoginInterceptor;
import site.wanjiahao.live.utils.PageUtils;
import site.wanjiahao.live.utils.Query;

import site.wanjiahao.live.dao.UserLiveDao;
import site.wanjiahao.live.entity.UserLiveEntity;
import site.wanjiahao.live.service.UserLiveService;


@Service("userLiveService")
@CacheConfig(cacheNames = Constant.LIVE_USER_CACHE_PREFIX)
public class UserLiveServiceImpl extends ServiceImpl<UserLiveDao, UserLiveEntity> implements UserLiveService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<UserLiveEntity> page = this.page(
                new Query<UserLiveEntity>().getPage(params),
                new QueryWrapper<UserLiveEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void follow(String liveId) {
        UserEntity userEntity = LoginInterceptor.threadLocal.get();
        if (userEntity != null) {
            UserLiveEntity userLiveEntity = new UserLiveEntity();
            userLiveEntity.setLid(liveId);
            userLiveEntity.setUid(userEntity.getId());
            // 判断当前是否存在 存在就删除
            UserLiveEntity result = this.selectByLidAndUid(liveId, userEntity.getId());
            if (result != null) {
                this.deleteByLidAndUid(liveId, userEntity.getId());
            } else {
                this.diyInsert(userLiveEntity);
            }
        } else {
            throw new AuthException("未登录");
        }
    }

    @Cacheable(key = "'userLiveEntity-one' + #p0 + '-' + #p1")
    @Override
    public UserLiveEntity selectByLidAndUid(String liveId, String id) {
        return baseMapper.selectOne(new QueryWrapper<UserLiveEntity>().eq("uid", id).eq("lid", liveId));
    }

    @CacheEvict(allEntries = true)
    @Override
    public void deleteByLidAndUid(String liveId, String id) {
        baseMapper.delete(new QueryWrapper<UserLiveEntity>().eq("uid", id).eq("lid", liveId));
    }

    @CacheEvict(allEntries = true)
    @Override
    public void diyInsert(UserLiveEntity userLiveEntity) {
        baseMapper.insert(userLiveEntity);
    }

    @Cacheable(key = "'userLiveEntity-one' + #p0")
    @Override
    public UserLiveEntity diyGetById(String uid) {
        return getById(uid);
    }

    @CacheEvict(allEntries = true)
    @Override
    public void diyUpdateById(UserLiveEntity userLive) {
        updateById(userLive);
    }

    @CacheEvict(allEntries = true)
    @Override
    public void diyRemoveByIds(List<String> asList) {
        removeByIds(asList);
    }

    @Cacheable(key = "'userIds-all' + #p0")
    @Override
    public Set<String> listUserIdByLiveId(String id) {
        return baseMapper.listUserIdByLiveId(id);
    }

}