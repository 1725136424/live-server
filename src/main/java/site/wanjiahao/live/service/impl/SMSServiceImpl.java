package site.wanjiahao.live.service.impl;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.ReUtil;
import com.alibaba.cloud.spring.boot.sms.SmsServiceImpl;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.exceptions.ClientException;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import site.wanjiahao.live.constant.Constant;
import site.wanjiahao.live.entity.UserEntity;
import site.wanjiahao.live.enumerate.UserStatus;
import site.wanjiahao.live.exception.RandomCodeException;
import site.wanjiahao.live.exception.RegexException;
import site.wanjiahao.live.service.SMSService;
import site.wanjiahao.live.utils.CommonUtils;
import site.wanjiahao.live.utils.R;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

@Service
public class SMSServiceImpl implements SMSService {

    @Autowired
    private SmsServiceImpl smsService;

    // ISO8859-1
    @Value("${sample.sign-name}")
    private String signName;

    @Value("${sample.template-code}")
    private String templateCode;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public void sendSMSWithRandom(String phone) throws ClientException {
        // 取出当前验证码，解决防刷功能
        String redisCode = stringRedisTemplate.opsForValue().get(Constant.CODE_PREFIX);
        if (!StringUtils.isBlank(redisCode)) {
            long timeLong = Long.parseLong(redisCode.split("_")[0]);
            // 取出当前时间
            long currentTimeMillis = System.currentTimeMillis();
            if (currentTimeMillis <= timeLong) {
                throw new RandomCodeException("验证码发送次数太多");
            }
        }
        // 生成随机验证码
        String randomCode = RandomUtil.randomNumbers(6);
        // 保存redis中 前缀 live:code:phone 值 randomCode 过期时间 60s (并且解决的验证码防刷的功能)
        stringRedisTemplate
                .opsForValue()
                .set(Constant.CODE_PREFIX + ":" + phone,
                        System.currentTimeMillis() + (Constant.CODE_PROTECT_BRUSH_INTERVAL * 1000) + "_" + randomCode,
                        Constant.CODE_EXPIRE, TimeUnit.SECONDS);

        SendSmsRequest sendSmsRequest = new SendSmsRequest();
        sendSmsRequest.setPhoneNumbers(phone);
        // 解决properties乱码问题
        byte[] bytes = signName.getBytes(StandardCharsets.ISO_8859_1);
        signName = new String(bytes, StandardCharsets.UTF_8);
        sendSmsRequest.setSignName(signName);
        sendSmsRequest.setTemplateCode(templateCode);
        sendSmsRequest.setTemplateParam("{\"code\": \"" + randomCode + "\"}");
        smsService.sendSmsRequest(sendSmsRequest);
    }

    @Override
    public boolean verifyCode(String code, String phone) {
        String redisCode = stringRedisTemplate.opsForValue().get(Constant.CODE_PREFIX + ":" + phone);
        if (code != null) {
            redisCode = redisCode.split("_")[1];
            return code.equals(redisCode);
        }
        return false;
    }

}
