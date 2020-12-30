package site.wanjiahao.live.vo;

import lombok.Data;

import java.util.Set;

@Data
public class LiveHallVo {

    /**
     * 直播间号
     */
    private String id;

    /**
     * 直播标题
     */
    private String title;

    /**
     * 直播实时图片
     */
    private String livePic;

    /**
     * 用户图片
     */
    private String userPic;

    /**
     * 用户昵称
     */
    private String username;

    /**
     * 热度
     */
    private Long hot;

    /**
     * 播流地址
     */
    private String playUrl;

}
