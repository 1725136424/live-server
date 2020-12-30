package site.wanjiahao.live.enumerate;

/**
 * 用户状态0-普通用户 1-注册主播 2-管理员
 */
public enum UserStatus {

    NORMAL(0, "普通用户"),
    VERIFY(1, "待审核"),
    ANCHOR(2, "主播"),
    ADMIN(3, "管理员");

    private final int code;

    private final String msg;

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    UserStatus(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

}
