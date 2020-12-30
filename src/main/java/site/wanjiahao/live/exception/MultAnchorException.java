package site.wanjiahao.live.exception;

/**
 * 多次提交主播注册，或者已经注册异常
 */
public class MultAnchorException extends RuntimeException {

    public MultAnchorException(String message) {
        super(message);
    }

}
