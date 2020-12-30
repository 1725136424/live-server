package site.wanjiahao.live.exception;

/**
 * 不是主播异常
 */
public class NoAnchorException extends RuntimeException {

    public NoAnchorException(String message) {
        super(message);
    }

}
