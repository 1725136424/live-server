package site.wanjiahao.live.enumerate;

public enum TranscodeEnum {
    NO("normal"),
    LD("ld"),
    SD("sd"),
    HD("hd"),
    UD("ud");
    TranscodeEnum(String name) {
        this.name = name;
    }
    private  final String name;

    public String getName() {
        return name;
    }
}
