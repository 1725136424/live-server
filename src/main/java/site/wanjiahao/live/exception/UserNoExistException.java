package site.wanjiahao.live.exception;

/**
 * 用户不存在异常
 */
public class UserNoExistException extends RuntimeException {

    public UserNoExistException(String message) {
        super(message);
    }

}
