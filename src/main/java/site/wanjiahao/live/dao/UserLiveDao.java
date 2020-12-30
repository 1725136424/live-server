package site.wanjiahao.live.dao;

import site.wanjiahao.live.entity.UserLiveEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.Set;

/**
 * 
 * 
 * @author haodada
 * @email 1725136424@qq.com
 * @date 2020-12-26 16:39:16
 */
@Mapper
public interface UserLiveDao extends BaseMapper<UserLiveEntity> {

    Set<String> listUserIdByLiveId(String id);

}
