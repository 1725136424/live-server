package site.wanjiahao.live.utils;

import site.wanjiahao.live.constant.Constant;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CookieUtils {
    /**
     * 设置cookie有效期，根据需要自定义[本系统设置为7天]
     */
    private final static int COOKIE_MAX_AGE = 60 * 60 * 24 * 7;

    /**
     * 设置cookie域，默认为：live.com
     */
//    private final static String COOKIE_DOMAIN = "wanjiahao.site";

    private final static String COOKIE_DOMAIN = "live.com";

    /**
     * 设置默认路径：/，这个路径即该工程下都可以访问该cookie 如果不设置路径，那么只有设置该cookie路径及其子路径可以访问
     */
    private final static String COOKIE_PATH = "/";

    /**
     *
     * @desc 删除指定Cookie
     * @param response
     * @param cookie
     */
    public static void removeCookie(HttpServletResponse response, Cookie cookie) {
        if (cookie != null) {
            cookie.setPath(COOKIE_PATH);
            cookie.setValue("");
            cookie.setMaxAge(0);
            cookie.setDomain(COOKIE_DOMAIN);
            response.addCookie(cookie);
        }
    }
    /**
     * @desc 删除指定名称的Cookie
     * @param response
     */
    public static void removeCookie(HttpServletResponse response, String name) {
        Cookie cookie = new Cookie(name,"");
        cookie.setPath(COOKIE_PATH);
        cookie.setMaxAge(0);
        cookie.setDomain(COOKIE_DOMAIN);
        response.addCookie(cookie);
    }


    /**
     * @desc 删除指定Cookie
     * @param response
     * @param cookie
     * @param domain
     */
    public static void removeCookie(HttpServletResponse response, Cookie cookie, String domain) {
        if (cookie != null) {
            cookie.setPath(COOKIE_PATH);
            cookie.setValue("");
            cookie.setMaxAge(0);
            cookie.setDomain(domain);
            response.addCookie(cookie);
        }
    }

    /**
     * @desc 删除指定名称的Cookie
     * @param response
     */
    public static void removeCookie(HttpServletResponse response, String name, String domain) {
        Cookie cookie = new Cookie(name,"");
        cookie.setPath(COOKIE_PATH);
        cookie.setMaxAge(0);
        cookie.setDomain(domain);
        response.addCookie(cookie);
    }


    /**
     *
     * @desc 根据Cookie名称得到Cookie的值，没有返回Null
     * @param request
     * @param name
     * @return
     */
    public static String getCookieValue(HttpServletRequest request, String name) {
        Cookie cookie = getCookie(request, name);
        if (cookie != null) {
            return cookie.getValue();
        }else{
            return null;
        }
    }


    /**
     *
     * @desc 根据Cookie名称得到Cookie对象，不存在该对象则返回Null
     * @param request
     * @param name
     */
    public static Cookie getCookie(HttpServletRequest request, String name) {
        Cookie cookies[] = request.getCookies();
        if (cookies == null || name == null || name.length() == 0){
            return null;
        }
        Cookie cookie = null;
        for (int i = 0; i < cookies.length; i++) {
            if (!cookies[i].getName().equals(name)){
                continue;
            }
            cookie = cookies[i];
            if (request.getServerName().equals(cookie.getDomain())){
                break;
            }
        }
        return cookie;
    }


    /**
     * @desc 添加一条新的Cookie信息，默认有效时间为一周
     * @param response
     * @param name
     * @param value
     */
    public static void setCookie(HttpServletResponse response, String name, String value) {
        setCookie(response, name, value, COOKIE_MAX_AGE);
    }

    /**
     * @desc 添加一条新的Cookie信息，默认有效时间为一周
     * @param response
     * @param name
     * @param value
     */
    public static void setCookie(HttpServletResponse response, String name, String value,String domain) {
        setCookie(response, name, value, COOKIE_MAX_AGE, domain);
    }


    /**
     * @desc 添加一条新的Cookie信息，可以设置其最长有效时间(单位：秒)
     * @param response
     * @param name
     * @param value
     * @param maxAge
     */
    public static void setCookie(HttpServletResponse response, String name, String value, int maxAge) {
        setCookie(response, name, value, maxAge, COOKIE_DOMAIN);
    }

    /**
     *
     * @desc 添加一条新的Cookie信息，可以设置其最长有效时间(单位：秒)
     * @param response
     * @param name
     * @param value
     * @param maxAge
     * @param domain
     */
    public static void setCookie(HttpServletResponse response, String name, String value, int maxAge, String domain) {
        if (value == null){
            value = "";
        }
        Cookie cookie = new Cookie(name, value);
        if(maxAge!=0){
            cookie.setMaxAge(maxAge);
        }else{
            cookie.setMaxAge(COOKIE_MAX_AGE);
        }
        cookie.setPath(COOKIE_PATH);
        if (domain!=null&&!domain.equals("")){
            cookie.setDomain(domain);
        }
        response.addCookie(cookie);
    }
}
