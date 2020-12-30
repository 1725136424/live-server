package site.wanjiahao.live.service.impl;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import site.wanjiahao.live.constant.Constant;
import site.wanjiahao.live.utils.PageUtils;
import site.wanjiahao.live.utils.Query;
import site.wanjiahao.live.dao.CategoryDao;
import site.wanjiahao.live.entity.CategoryEntity;
import site.wanjiahao.live.service.CategoryService;


@Service("categoryService")
@CacheConfig(cacheNames = Constant.CATEGORY_CACHE_PREFIX)
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryEntity> page = this.page(
                new Query<CategoryEntity>().getPage(params),
                new QueryWrapper<CategoryEntity>()
        );

        return new PageUtils(page);
    }

    @Cacheable(key = "'categoryEntities-all'")
    @Override
    public List<CategoryEntity> listAll() {
        return baseMapper.selectList(null);
    }

    @Cacheable(key = "'categoryEntity-one' + #p0")
    @Override
    public CategoryEntity findById(String id) {
        return getById(id);
    }

    @CacheEvict(allEntries = true)
    @Override
    public void diySave(CategoryEntity category) {
        save(category);
    }

    @CacheEvict(allEntries = true)
    @Override
    public void diyUpdateById(CategoryEntity category) {
        updateById(category);
    }

    @CacheEvict(allEntries = true)
    @Override
    public void diyRemoveByIds(List<String> asList) {
        removeByIds(asList);
    }

}