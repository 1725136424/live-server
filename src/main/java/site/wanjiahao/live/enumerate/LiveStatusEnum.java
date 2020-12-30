package site.wanjiahao.live.enumerate;

public enum LiveStatusEnum {

    PUBLISH_DONE(0, "publish_done"),
    PUBLISH(1, "publish");

    private final int code;

    private final String status;

    public int getCode() {
        return code;
    }

    public String getStatus() {
        return status;
    }

    LiveStatusEnum(int code, String status) {
        this.code = code;
        this.status = status;
    }
}
