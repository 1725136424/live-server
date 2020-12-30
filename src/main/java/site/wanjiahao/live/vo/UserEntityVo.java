package site.wanjiahao.live.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import site.wanjiahao.live.constant.Constant;
import site.wanjiahao.live.valid.group.SaveGroup;
import site.wanjiahao.live.valid.group.UpdateGroup;

import javax.validation.constraints.*;

/**
 *
 *
 * @author haodada
 * @email 1725136424@qq.com
 * @date 2020-12-26 16:39:16
 */
@Data
public class UserEntityVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Null(message = "id必须为空")
    private String id;

    /**
     * 用户昵称
     */
    @NotBlank(message = "昵称不能为空")
    private String username;
    /**
     * 用户密码
     */
    @NotBlank(message = "密码不能为空")
    @Size(min = 8, max = 15, message = "密码长度不符合")
    private String password;
    /**
     * 用户头像
     */
    @NotBlank(message = "头像不能为空")
    @Pattern(regexp = Constant.REG_URL,
            message = "图片地址格式不正确")
    private String pic;
    /**
     * 手机号
     */
    @NotBlank(message = "手机号不能为空")
    @Pattern(regexp = Constant.REG_PHONE,
            message = "手机号格式不正确")
    private String phone;

    /**
     * 验证码
     */
    @NotBlank
    @Size(min = 6, max = 6, message = "必须为6位验证码")
    private String code;

}

