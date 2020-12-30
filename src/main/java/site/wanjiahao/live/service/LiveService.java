package site.wanjiahao.live.service;

import com.baomidou.mybatisplus.extension.service.IService;
import site.wanjiahao.live.entity.LiveEntity;
import site.wanjiahao.live.utils.PageUtils;
import site.wanjiahao.live.vo.LiveVo;
import site.wanjiahao.live.vo.StartAnchorVo;
import site.wanjiahao.live.vo.StreamUrlVo;

import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author haodada
 * @email 1725136424@qq.com
 * @date 2020-12-26 16:39:16
 */
public interface LiveService extends IService<LiveEntity> {

    PageUtils queryPage(Map<String, Object> params);

    StreamUrlVo startAnchor(StartAnchorVo startAnchorVo);

    boolean roomIsExist(String id);

    LiveEntity findByAppNameAndStreamName(String appName, String id);

    LiveVo findLiveVoById(String id);

    LiveEntity diyGetById(String id);

    void diySave(LiveEntity live);

    LiveEntity findByIdCard(String id_card);

    void diyUpdateById(LiveEntity liveEntity);

    void diyRemoveByIds(List<String> asList);

    LiveEntity findById(String id);
}

