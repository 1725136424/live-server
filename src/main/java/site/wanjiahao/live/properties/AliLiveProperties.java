package site.wanjiahao.live.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "alibaba.live")
public class AliLiveProperties {

    // endpoint
    private String endpoint;

    // accessId
    private String accessId;

    // accessKey
    private String accessKey;

    // 推流域名
    private String pushDomain;

    // 播流域名
    private String playDomain;

    // 推流key
    private String pushKey;

    // 播流key
    private String playKey;

    // appName
    private String appName;

    // steamName
    private String streamName;

    // 过期时间 秒为单位
    private Integer expireTime;

    // 图片访问前缀
    private String picSuffix;

}
