package site.wanjiahao.live.exception;

/**
 * 无权限
 */
public class NoPermissionException extends RuntimeException {

    public NoPermissionException(String message) {
        super(message);
    }

}
