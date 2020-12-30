package site.wanjiahao.live.exception;

/**
 * 手机号存在
 */
public class PhoneExistException extends RuntimeException {

    public PhoneExistException(String message) {
        super(message);
    }

}
