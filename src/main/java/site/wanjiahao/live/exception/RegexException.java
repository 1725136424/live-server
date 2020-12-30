package site.wanjiahao.live.exception;

/**
 * 自定义正则匹配一次
 */
public class RegexException extends RuntimeException {

    public RegexException(String message) {
        super(message);
    }

}
