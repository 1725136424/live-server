package site.wanjiahao.live.enumerate;

public enum  StatusCodeEnum {

    RANDOM_NOT_MATCH(10000, "验证码不匹配"),
    RANDOM_SEND_SHORT_INTERVAL(10001, "验证码发送次数太多"),
    PHONE_FORMAT_EXCEPTION(10002, "手机号格式不正确"),
    PHONE_IS_EXIST(20000, "手机已经存在"),
    AUTH_ERROR(20001, "登录错误"),
    LOGINED(20002, "已经登录"),
    PHONE_NOT_EXIST(20003, "手机号不存在"),
    NO_AUTH_EXCEPTION(20003, "未认证"),
    USER_NOT_EXIST_EXCEPTION(20004, "没有此用户") ,
    NO_PERMISSION_EXCEPTION(20005, "无权限操作"),
    ANCHOR_MULT_EXCEPTION(30001, "多次提交主播注册，或者已经注册"),
    NO_ANCHOR_EXCEPTION(30002, "当前用户不是主播"),
    ROOM_IS_EXIST_EXCEPTION(40000, "房间号已经存在"),
    STATUS_DISACCORD_EXCEPTION(88888, "状态不一致");

    private final int code;

    private final String msg;

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    StatusCodeEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

}
