package site.wanjiahao.live.exception;

/**
 * 手机号不存在
 */
public class PhoneNotExistException extends RuntimeException {

    public PhoneNotExistException(String message) {
        super(message);
    }

}
