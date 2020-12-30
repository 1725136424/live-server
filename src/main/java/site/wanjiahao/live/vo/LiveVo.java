package site.wanjiahao.live.vo;

import lombok.Data;

import java.util.Set;

@Data
public class LiveVo {

    private String userPic;

    private String username;

    private String title;

    private Long hot;

    private String playUrl;

    private Set<String> ids;
}
