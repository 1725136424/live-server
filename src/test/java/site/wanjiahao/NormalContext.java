package site.wanjiahao;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import site.wanjiahao.live.enumerate.PlayProtocolEnum;
import site.wanjiahao.live.exception.RandomCodeException;


public class NormalContext {

    // 推流域名
    private static final String pushDomain = "push.wanjiahao.site";

    // 播流域名
    private static final String playDomain = "play.wanjiahao.site";

    // 推流key
    private static final String pushKey = "NhACL7X1dK";

    // 播流key
    private static final String playKey = "X1lRHR0NWn";

    // appName
    private static final String appName = "app";

    // steamName
    private static final String streamName = "name";

    // 过期时间 秒为单位
    private static final Integer expireTime = 3600;


    private static String createPushUrl(String appName, String streamName, String domain, long exp, String key) {
        // 构建推流地址
        String hashValue = StrUtil.format("/{}/{}-{}-0-0-{}", appName, streamName, System.currentTimeMillis() / 1000 + exp, key);
        // md5
        String md5 = DigestUtil.md5Hex(hashValue);
        return StrUtil.format("rtmp://{}/{}/{}?auth_key={}-0-0-{}", domain, appName, streamName, System.currentTimeMillis() / 1000 + exp, md5);
    }

    /*
     * RTMP:rtmp://播流域名/AppName/StreamName?鉴权串
     * FLV:http://播流域名/AppName/StreamName.flv?鉴权串
     * HLS:http://播流域名/AppName/StreamName.m3u8?鉴权串
     * UDP:artc://播流域名/AppName/StreamName?鉴权串
     * */
    private static String createPlayUrl(String appName, String streamName, String domain, long exp, String key) {
        return createPlayUrl(appName, streamName, domain, exp, key, PlayProtocolEnum.M3U8);
    }

    private static String createPlayUrl(String appName, String streamName, String domain, long exp, String key, PlayProtocolEnum playProtocolEnum) {
        if (playProtocolEnum == null) {
            throw new RuntimeException("传输协议类型不能为空");
        } else {
            switch (playProtocolEnum.getName()) {
                case "RTMP":
                    String uri = StrUtil.format("/{}/{}-{}-0-0-{}", appName, streamName, System.currentTimeMillis() / 1000 + exp, key);
                    String md5Uri = DigestUtil.md5Hex(uri);
                    return StrUtil.format("rtmp://{}/{}/{}?auth_key={}-0-0-{}", domain, appName, streamName, System.currentTimeMillis() / 1000 + exp, md5Uri);
                case "FLV":
                    String flvUri = StrUtil.format("/{}/{}.flv-{}-0-0-{}", appName, streamName, System.currentTimeMillis() / 1000 + exp, key);
                    String md5Flv = DigestUtil.md5Hex(flvUri);
                    return StrUtil.format("https://{}/{}/{}.flv?auth_key={}-0-0-{}", domain, appName, streamName, System.currentTimeMillis() / 1000 + exp, md5Flv);
                case "UDP":
                    String udpUri = StrUtil.format("/{}/{}-{}-0-0-{}", appName, streamName, System.currentTimeMillis() / 1000 + exp, key);
                    String udpMd5 = DigestUtil.md5Hex(udpUri);
                    return StrUtil.format("artc://{}/{}/{}?auth_key={}-0-0-{}", domain, appName, streamName, System.currentTimeMillis() / 1000 + exp, udpMd5);
                default:
                    String m3u8Uri = StrUtil.format("/{}/{}.m3u8-{}-0-0-{}", appName, streamName, System.currentTimeMillis() / 1000 + exp, key);
                    String md5M3u8 = DigestUtil.md5Hex(m3u8Uri);
                    return StrUtil.format("https://{}/{}/{}.m3u8?auth_key={}-0-0-{}", domain, appName, streamName, System.currentTimeMillis() / 1000 + exp, md5M3u8);
            }
        }
    }


    public static void main(String[] args) {
      /*  *//*275682e2c8b52e35f51eb6015584f135*//*
        String password = CommonUtils.MD5Hex("Ww13684819080", Constant.SALT, Constant.num);
        System.out.println(password);*/

        RandomCodeException randomCodeException = new RandomCodeException("`111");
        System.out.println(randomCodeException.getClass().getSimpleName());
    }
}
