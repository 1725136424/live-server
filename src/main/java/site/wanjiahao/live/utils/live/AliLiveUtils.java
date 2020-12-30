package site.wanjiahao.live.utils.live;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.live.model.v20161101.AddLiveStreamTranscodeRequest;
import com.aliyuncs.live.model.v20161101.AddLiveStreamTranscodeResponse;
import com.aliyuncs.live.model.v20161101.DescribeLiveStreamOnlineUserNumRequest;
import com.aliyuncs.live.model.v20161101.DescribeLiveStreamOnlineUserNumResponse;
import com.aliyuncs.profile.DefaultProfile;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;
import site.wanjiahao.live.enumerate.PlayProtocolEnum;
import site.wanjiahao.live.enumerate.TranscodeEnum;
import site.wanjiahao.live.properties.AliLiveProperties;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
@EnableConfigurationProperties(AliLiveProperties.class)
@Slf4j
public class AliLiveUtils {

    @Autowired
    private AliLiveProperties aliLiveProperties;

    public String createPushUrl(String appName, String streamName) {
        Integer exp = aliLiveProperties.getExpireTime();
        String key = aliLiveProperties.getPushKey();
        String domain = aliLiveProperties.getPushDomain();
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
    public List<String> createPlayUrl(String appName, String streamName) {
        return createPlayUrl(appName, streamName, PlayProtocolEnum.M3U8);
    }

    public List<String> createPlayUrl(String appName, String streamName, PlayProtocolEnum playProtocolEnum) {
        Integer exp = aliLiveProperties.getExpireTime();
        String key = aliLiveProperties.getPlayKey();
        String domain = aliLiveProperties.getPlayDomain();
        if (playProtocolEnum == null) {
            throw new RuntimeException("传输协议类型不能为空");
        } else {
            // 获取转码枚举
            TranscodeEnum[] values = TranscodeEnum.values();
            switch (playProtocolEnum.getName()) {
                case "RTMP":
                    return Arrays.stream(values).map((item) -> {
                        String str = "";
                        if (!TranscodeEnum.NO.getName().equals(item.getName())) {
                            str += "_" + item.getName();
                        }
                        String uri = StrUtil.format("/{}/{}"+ str +"-{}-0-0-{}", appName, streamName, System.currentTimeMillis() / 1000 + exp, key);
                        String md5Uri = DigestUtil.md5Hex(uri);
                        return StrUtil.format("rtmp://{}/{}/{}"+ str +"?auth_key={}-0-0-{}", domain, appName, streamName, System.currentTimeMillis() / 1000 + exp, md5Uri);
                    }).collect(Collectors.toList());
                case "FLV":
                    return Arrays.stream(values).map((item) -> {
                        String str = "";
                        if (!TranscodeEnum.NO.getName().equals(item.getName())) {
                            str += "_" + item.getName();
                        }
                        String flvUri = StrUtil.format("/{}/{}"+ str +".flv-{}-0-0-{}", appName, streamName, System.currentTimeMillis() / 1000 + exp, key);
                        String md5Flv = DigestUtil.md5Hex(flvUri);
                        return StrUtil.format("https://{}/{}/{}"+ str +".flv?auth_key={}-0-0-{}", domain, appName, streamName, System.currentTimeMillis() / 1000 + exp, md5Flv);
                    }).collect(Collectors.toList());
                case "UDP":
                    return Arrays.stream(values).map((item) -> {
                        String str = "";
                        if (!TranscodeEnum.NO.getName().equals(item.getName())) {
                            str += "_" + item.getName();
                        }
                        String udpUri = StrUtil.format("/{}/{}"+ str +"-{}-0-0-{}", appName, streamName, System.currentTimeMillis() / 1000 + exp, key);
                        String md5Udp = DigestUtil.md5Hex(udpUri);
                        return StrUtil.format("artc://{}/{}/{}"+ str +"?auth_key={}-0-0-{}", domain, appName, streamName, System.currentTimeMillis() / 1000 + exp, md5Udp);
                    }).collect(Collectors.toList());
                default:
                    return Arrays.stream(values).map((item) -> {
                        String str = "";
                        if (!TranscodeEnum.NO.getName().equals(item.getName())) {
                            str += "_" + item.getName();
                        }
                        String m3u8Uri = StrUtil.format("/{}/{}"+ str +".m3u8-{}-0-0-{}", appName, streamName, System.currentTimeMillis() / 1000 + exp, key);
                        String md5M3u8 = DigestUtil.md5Hex(m3u8Uri);
                        return StrUtil.format("https://{}/{}/{}"+ str +".m3u8?auth_key={}-0-0-{}", domain, appName, streamName, System.currentTimeMillis() / 1000 + exp, md5M3u8);
                    }).collect(Collectors.toList());
            }
        }
    }

    /**
     * 统计在线人数
     * @param appName
     * @param streamName
     * @return
     */
    public Long sumOnlineNumbers(String appName, String streamName) {
        DefaultProfile profile = DefaultProfile.getProfile(
                aliLiveProperties.getEndpoint(),
                aliLiveProperties.getAccessId(),
                aliLiveProperties.getAccessKey());

        IAcsClient client = new DefaultAcsClient(profile);
        DescribeLiveStreamOnlineUserNumRequest request = new DescribeLiveStreamOnlineUserNumRequest();
        // 直播流所属应用名称。
        request.setAppName(appName);
        // 直播流名称。
        request.setStreamName(streamName);
        // 您的加速域名。DomainName为必选参数。
        request.setDomainName(aliLiveProperties.getPlayDomain());
        try {
            DescribeLiveStreamOnlineUserNumResponse response = client.getAcsResponse(request);
            /*{"requestId":"41AD96B4-89A7-4B3E-8649-B911540C647E","totalUserNumber":0,"onlineUserInfo":[]}*/
            return response.getTotalUserNumber();
        } catch (
                ServerException e) {
            e.printStackTrace();
        } catch (
                ClientException e) {
            System.out.println("ErrCode:" + e.getErrCode());
            System.out.println("ErrMsg:" + e.getErrMsg());
            System.out.println("RequestId:" + e.getRequestId());
        }
        return 0L;
    }

    /**
     * 计算热度
     */
    public Long sumHot(String appName, String streamName) {
        Long number = sumOnlineNumbers(appName, streamName);
        log.info("真实在线人数---{}---{}", appName, number);
        int random = RandomUtil.randomInt(4, 6);
        // 计算热度
        return number + random;
    }

    /**
     * 构造转码模板
     */
    public void buildTranscode(String appName, TranscodeEnum transcodeEnum) {
        DefaultProfile profile = DefaultProfile.getProfile(
                aliLiveProperties.getEndpoint(),
                aliLiveProperties.getAccessId(),
                aliLiveProperties.getAccessKey());

        IAcsClient client = new DefaultAcsClient(profile);

        AddLiveStreamTranscodeRequest request = new AddLiveStreamTranscodeRequest();
        request.setRegionId(aliLiveProperties.getEndpoint());
        request.setApp(appName);
        request.setTemplate(transcodeEnum.getName());
        request.setDomain(aliLiveProperties.getPlayDomain());
        try {
            AddLiveStreamTranscodeResponse response = client.getAcsResponse(request);
            System.out.println(new Gson().toJson(response));
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            System.out.println("ErrCode:" + e.getErrCode());
            System.out.println("ErrMsg:" + e.getErrMsg());
            System.out.println("RequestId:" + e.getRequestId());
        }

    }
}
