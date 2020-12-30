package site.wanjiahao;

import com.aliyuncs.exceptions.ClientException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import site.wanjiahao.live.LiveApplication;
import site.wanjiahao.live.service.SMSService;

import java.io.UnsupportedEncodingException;

@SpringBootTest(classes = {LiveApplication.class})
public class Context {



    @Autowired
    private SMSService smsService;

    @Test
    public void test() throws ClientException, UnsupportedEncodingException {
        smsService.sendSMSWithRandom("18307008805");
    }

}
