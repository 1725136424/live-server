package site.wanjiahao.live.config;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.profile.DefaultProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import site.wanjiahao.live.properties.AliLiveProperties;

@Configuration
@EnableConfigurationProperties(AliLiveProperties.class)
public class AliLiveConfig {

    @Autowired
    private AliLiveProperties aliLiveProperties;

    @Bean
    public IAcsClient iAcsClient() {
        DefaultProfile profile = DefaultProfile.getProfile(aliLiveProperties.getEndpoint(),
                aliLiveProperties.getAccessId(),
                aliLiveProperties.getAccessKey());
        return new DefaultAcsClient(profile);
    }

}
