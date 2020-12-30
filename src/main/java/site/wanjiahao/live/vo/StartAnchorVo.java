package site.wanjiahao.live.vo;

import lombok.Data;
import javax.validation.constraints.NotBlank;

@Data
public class StartAnchorVo {

    @NotBlank(message = "标题不能为空")
    private String title;

    @NotBlank(message = "分类不能为空")
    private String category;
}
