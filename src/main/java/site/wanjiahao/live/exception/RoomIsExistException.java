package site.wanjiahao.live.exception;

/**
 * 房间号已经存在异常
 */
public class RoomIsExistException extends RuntimeException {

    public RoomIsExistException(String message) {
        super(message);
    }

}
