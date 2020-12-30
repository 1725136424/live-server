package site.wanjiahao.live.enumerate;

public enum PlayProtocolEnum {

    RTMP("RTMP"),
    FLV("FLV"),
    M3U8("M3U8"),
    UDP("UDP");
    PlayProtocolEnum(String name) {
        this.name = name;
    }
    private  final String name;

    public String getName() {
        return name;
    }
}
