package site.wanjiahao.live.controller;

import cn.hutool.core.util.ReUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.wanjiahao.live.constant.Constant;
import site.wanjiahao.live.enumerate.StatusCodeEnum;
import site.wanjiahao.live.exception.RandomCodeException;
import site.wanjiahao.live.exception.RegexException;
import site.wanjiahao.live.service.SMSService;
import site.wanjiahao.live.utils.R;


@RestController
@RequestMapping("/sms")
@Slf4j
public class SMSController {

    @Autowired
    private SMSService smsService;

    @GetMapping("/sendCode/{phone}")
    public R sendCode(@PathVariable("phone") String phone) {
        try {
            boolean match = ReUtil.isMatch(Constant.REG_PHONE, phone);
            if (match) {
                smsService.sendSMSWithRandom(phone);
            } else {
                return R.error(StatusCodeEnum.PHONE_FORMAT_EXCEPTION.getCode(), StatusCodeEnum.PHONE_FORMAT_EXCEPTION.getMsg());
            }
            return R.ok();
        } catch (RandomCodeException e) {
            log.error("发送短信太多{}", e.getMessage());
            return R.error(StatusCodeEnum.RANDOM_SEND_SHORT_INTERVAL.getCode(), StatusCodeEnum.RANDOM_SEND_SHORT_INTERVAL.getMsg());
        } catch (Exception e) {
            e.printStackTrace();
            log.error("发送短信接口错误{}", e.getMessage());
            return R.error();
        }
    }
}
