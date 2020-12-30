package site.wanjiahao.live.controller;

import java.util.Arrays;
import java.util.Map;

// import org.apache.shiro.authz.annotation.RequiresPermissions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import site.wanjiahao.live.constant.Constant;
import site.wanjiahao.live.entity.LiveEntity;
import site.wanjiahao.live.enumerate.LiveStatusEnum;
import site.wanjiahao.live.enumerate.StatusCodeEnum;
import site.wanjiahao.live.exception.NoAnchorException;
import site.wanjiahao.live.service.LiveService;
import site.wanjiahao.live.utils.PageUtils;
import site.wanjiahao.live.utils.R;
import site.wanjiahao.live.vo.LiveVo;
import site.wanjiahao.live.vo.PushStreamVo;
import site.wanjiahao.live.vo.StartAnchorVo;
import site.wanjiahao.live.vo.StreamUrlVo;


/**
 * 
 *
 * @author haodada
 * @email 1725136424@qq.com
 * @date 2020-12-26 16:39:16
 */
@RestController
@RequestMapping("live/live")
@Slf4j
public class LiveController {
    @Autowired
    private LiveService liveService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    // @RequiresPermissions("live:live:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = liveService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    // @RequiresPermissions("live:live:info")
    public R info(@PathVariable("id") String id){
		LiveEntity live = liveService.diyGetById(id);

        return R.ok().put("live", live);
    }

    /**
     * 获取直播间页面数据
     */
    @GetMapping("/info_two/{id}")
    public R info_two(@PathVariable("id") String id){
        LiveVo live = liveService.findLiveVoById(id);
        return R.ok().put("live", live);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    // @RequiresPermissions("live:live:save")
    public R save(@RequestBody LiveEntity live){
		liveService.diySave(live);

        return R.ok();
    }

    /**
     * 开播
     */
    @PostMapping("/startAnchor")
    public R startAnchor(@Validated @RequestBody StartAnchorVo startAnchorVo) {
        try {
            StreamUrlVo streamUrlVo = liveService.startAnchor(startAnchorVo);
            return R.ok().put("stream", streamUrlVo);
        } catch (NoAnchorException e) {
            return R.error(StatusCodeEnum.NO_ANCHOR_EXCEPTION.getCode(), StatusCodeEnum.NO_ANCHOR_EXCEPTION.getMsg());
        } catch (Exception e) {
            e.printStackTrace();
            log.error("开播接口发送错误{}", e.getMessage());
            return R.error();
        }
    }

    /**
     * 判断当前房间号是否存在
     */
    @GetMapping("/roomIsExist/{id}")
    public R roomIsExist(@PathVariable("id") String id) {
        try {
            boolean isExist = liveService.roomIsExist(id);
            return R.error().put("isExist", isExist);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("判断是否存在直播间接口出现错误{}", e.getMessage());
            return R.error().put("isExist", true);
        }
    }

    /**
     * 回调接收
     * &action=publish
     * &app=example.com
     * &appname=test01
     * &id=test01
     * &ip=42.120.xx.xx
     * &node=cdnvideocenter****16011.cm3
     * &time=1579027784
     * &usrargs=vhost%3Dmxl-****.cn%26auth_key%3
     */
    @GetMapping("/callback")
    public R callback(PushStreamVo pushStreamVo) {
        // 判断当前推流状态
        String action = pushStreamVo.getAction();
        // 根据appName id(streamName)获取直播间
        LiveEntity liveEntity = liveService.findByAppNameAndStreamName(pushStreamVo.getAppname(), pushStreamVo.getId());
        if (LiveStatusEnum.PUBLISH_DONE.getStatus().equals(action)) {
            // 断流
            liveEntity.setStatus(LiveStatusEnum.PUBLISH_DONE.getCode());
        } else if (LiveStatusEnum.PUBLISH.getStatus().equals(action)){
            // 推流
            liveEntity.setStatus(LiveStatusEnum.PUBLISH.getCode());
        }
        // 更新状态
        liveService.diyUpdateById(liveEntity);
        return R.ok();
    }
    /**
     * 修改
     */
    @RequestMapping("/update")
    // @RequiresPermissions("live:live:update")
    public R update(@RequestBody LiveEntity live){
		liveService.diyUpdateById(live);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    // @RequiresPermissions("live:live:delete")
    public R delete(@RequestBody String[] ids){
		liveService.diyRemoveByIds(Arrays.asList(ids));

        return R.ok();
    }

}
