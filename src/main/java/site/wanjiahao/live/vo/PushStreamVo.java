package site.wanjiahao.live.vo;

import lombok.Data;

@Data
public class PushStreamVo {
    /**
     * publish：推流
     * publish_done：断流
     */
    private String action;

    /**
     * 默认为自定义的推流域名，如果未绑定推流域名即为播流域名
     */
    private String app;

    /**
     * 应用名称
     */
    private String appname;

    /**
     * 流名称说明
     */
    private String id;

    /**
     * 推流客户端的地址ip
     */
    private String ip;

    /**
     * CDN接受流的节点或者机器名
     */
    private String node;

    /**
     * unix时间戳
     */
    private Long time;

    /**
     * 用户推流的参数
     */
    private String usrargs;
}
