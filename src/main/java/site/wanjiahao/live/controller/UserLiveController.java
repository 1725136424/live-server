package site.wanjiahao.live.controller;

import java.util.Arrays;
import java.util.Map;

// import org.apache.shiro.authz.annotation.RequiresPermissions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import site.wanjiahao.live.entity.UserLiveEntity;
import site.wanjiahao.live.enumerate.StatusCodeEnum;
import site.wanjiahao.live.exception.AuthException;
import site.wanjiahao.live.service.UserLiveService;
import site.wanjiahao.live.utils.PageUtils;
import site.wanjiahao.live.utils.R;



/**
 * 
 *
 * @author haodada
 * @email 1725136424@qq.com
 * @date 2020-12-26 16:39:16
 */
@RestController
@RequestMapping("live/userlive")
@Slf4j
public class UserLiveController {
    @Autowired
    private UserLiveService userLiveService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    // @RequiresPermissions("live:userlive:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = userLiveService.queryPage(params);

        return R.ok().put("page", page);
    }

    /**
     * 关注
     */
    @GetMapping("/follow/{liveId}")
    public R follow(@PathVariable("liveId") String liveId) {
        try {
            userLiveService.follow(liveId);
            return R.ok();
        } catch (AuthException authException) {
            return R.error(StatusCodeEnum.AUTH_ERROR.getCode(), StatusCodeEnum.AUTH_ERROR.getMsg());
        } catch (Exception e) {
            e.printStackTrace();
            log.error("关注接口出现错误{}", e.getMessage());
            return R.error();
        }
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{uid}")
    // @RequiresPermissions("live:userlive:info")
    public R info(@PathVariable("uid") String uid){
		UserLiveEntity userLive = userLiveService.diyGetById(uid);

        return R.ok().put("userLive", userLive);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    // @RequiresPermissions("live:userlive:save")
    public R save(@RequestBody UserLiveEntity userLive){
		userLiveService.diyInsert(userLive);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    // @RequiresPermissions("live:userlive:update")
    public R update(@RequestBody UserLiveEntity userLive){
		userLiveService.diyUpdateById(userLive);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    // @RequiresPermissions("live:userlive:delete")
    public R delete(@RequestBody String[] uids){
		userLiveService.diyRemoveByIds(Arrays.asList(uids));

        return R.ok();
    }

}
