package site.wanjiahao.live.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class AnchorVo {

    @NotBlank(message = "名字不能为空")
    @Pattern(regexp = "^[\\u4E00-\\u9FA5\\uf900-\\ufa2d·s]{2,20}$", message = "名字不合法")
    private String name;

    @NotBlank(message = "身份证不能为空")
    @Pattern(regexp = "\\d{17}[\\d|x]|\\d{15}", message = "身份证不合法")
    private String idCard;

}
