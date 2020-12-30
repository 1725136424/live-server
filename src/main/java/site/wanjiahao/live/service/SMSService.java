package site.wanjiahao.live.service;

import com.aliyuncs.exceptions.ClientException;

import java.io.UnsupportedEncodingException;

public interface SMSService {

    void sendSMSWithRandom(String phone) throws ClientException, UnsupportedEncodingException;

    boolean verifyCode(String code, String phone);

}
