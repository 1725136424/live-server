package site.wanjiahao.live.service;

import com.baomidou.mybatisplus.extension.service.IService;
import site.wanjiahao.live.utils.PageUtils;
import site.wanjiahao.live.entity.CategoryEntity;

import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author haodada
 * @email 1725136424@qq.com
 * @date 2020-12-26 16:39:16
 */
public interface CategoryService extends IService<CategoryEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<CategoryEntity> listAll();

    CategoryEntity findById(String id);

    void diySave(CategoryEntity category);

    void diyUpdateById(CategoryEntity category);

    void diyRemoveByIds(List<String> asList);
}

