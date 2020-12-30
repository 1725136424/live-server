package site.wanjiahao.live.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import site.wanjiahao.live.constant.Constant;
import site.wanjiahao.live.entity.CategoryEntity;
import site.wanjiahao.live.enumerate.StatusCodeEnum;
import site.wanjiahao.live.service.CategoryService;
import site.wanjiahao.live.utils.PageUtils;
import site.wanjiahao.live.utils.R;
import site.wanjiahao.live.valid.group.SaveGroup;

/**
 * 
 *
 * @author haodada
 * @email 1725136424@qq.com
 * @date 2020-12-26 16:39:16
 */
@RestController
@RequestMapping("live/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    // @RequiresPermissions("live:category:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = categoryService.queryPage(params);
        return R.ok().put("page", page);
    }

    /**
     * 查询所有缓存数数据
     */
    @GetMapping("/getAll")
    public R getAll() {
        List<CategoryEntity> categoryEntities = categoryService.listAll();
        return R.ok().put("categories", categoryEntities);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    // @RequiresPermissions("live:category:info")
    public R info(@PathVariable("id") String id){
		CategoryEntity category = categoryService.findById(id);

        return R.ok().put("category", category);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    // @RequiresPermissions("live:category:save")
    public R save(@Validated(SaveGroup.class) @RequestBody CategoryEntity category){
		categoryService.diySave(category);
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    // @RequiresPermissions("live:category:update")
    public R update(@RequestBody CategoryEntity category){
		categoryService.diyUpdateById(category);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    // @RequiresPermissions("live:category:delete")
    public R delete(@RequestBody String[] ids){
		categoryService.diyRemoveByIds(Arrays.asList(ids));

        return R.ok();
    }

}
