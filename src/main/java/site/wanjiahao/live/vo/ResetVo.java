package site.wanjiahao.live.vo;

import lombok.Data;
import site.wanjiahao.live.constant.Constant;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class ResetVo {

    @NotBlank(message = "手机号不能为空")
    @Pattern(regexp = Constant.REG_PHONE,
            message = "手机号格式不正确")
    private String phone;

    @NotBlank(message = "密码不能为空")
    @Size(min = 8, max = 15, message = "密码长度不符合")
    private String password;

    @NotBlank(message = "验证码不能为空")
    @Size(min = 6, max = 6, message = "验证码长度必须为6位")
    private String code;

}
