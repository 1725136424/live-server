package site.wanjiahao.live.service;

import com.baomidou.mybatisplus.extension.service.IService;
import site.wanjiahao.live.utils.PageUtils;
import site.wanjiahao.live.entity.UserLiveEntity;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 
 *
 * @author haodada
 * @email 1725136424@qq.com
 * @date 2020-12-26 16:39:16
 */
public interface UserLiveService extends IService<UserLiveEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void follow(String liveId);

    Set<String> listUserIdByLiveId(String id);

    UserLiveEntity selectByLidAndUid(String liveId, String id);

    void deleteByLidAndUid(String liveId, String id);

    void diyInsert(UserLiveEntity userLiveEntity);

    UserLiveEntity diyGetById(String uid);

    void diyUpdateById(UserLiveEntity userLive);

    void diyRemoveByIds(List<String> asList);
}

