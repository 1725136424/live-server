package site.wanjiahao.live.service;

import com.baomidou.mybatisplus.extension.service.IService;
import site.wanjiahao.live.utils.PageUtils;
import site.wanjiahao.live.entity.UserEntity;
import site.wanjiahao.live.vo.AnchorVo;
import site.wanjiahao.live.vo.LoginUserVo;
import site.wanjiahao.live.vo.ResetVo;

import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author haodada
 * @email 1725136424@qq.com
 * @date 2020-12-26 16:39:16
 */
public interface UserService extends IService<UserEntity> {

    PageUtils queryPage(Map<String, Object> params);

    boolean findExistByPhone(String phone);

    UserEntity login(LoginUserVo loginUserVo);

    void anchor(AnchorVo anchorVo);

    void passAnchor(String id);

    UserEntity findByIdCard(String idCard);

    UserEntity findById(String id);

    void resetPassword(ResetVo resetVo);

    UserEntity findByPhone(String phone);

    void diyUpdateById(UserEntity userEntity);

    void diyRemoveByIds(List<String> asList);

    UserEntity findByPhoneAndPassword(String phone, String password);
}

