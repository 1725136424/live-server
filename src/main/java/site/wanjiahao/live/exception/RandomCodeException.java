package site.wanjiahao.live.exception;

/**
 * 验证码设置一次
 */
public class RandomCodeException extends RuntimeException {

    public RandomCodeException(String message) {
        super(message);
    }

}
