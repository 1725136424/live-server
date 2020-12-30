package site.wanjiahao.live.interceptor;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import site.wanjiahao.live.constant.Constant;
import site.wanjiahao.live.entity.UserEntity;
import site.wanjiahao.live.enumerate.StatusCodeEnum;
import site.wanjiahao.live.utils.R;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class LoginInterceptor implements HandlerInterceptor {

    public static final ThreadLocal<UserEntity> threadLocal = new ThreadLocal<>();

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    public static final String[] authUrls = new String[]{
            "/live/user/logout", // 退出
            "/live/user/anchor", // 注册主播
            "/live/user/passAnchor/{id}", // 审核通过主播
            "/live/live/startAnchor", // 开启直播，获取推流地址
            "/live/userlive/follow/{liveId}" // 关注主播
    };

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        // 判断当前请求路径是否为需要登录的路径
        String requestURI = request.getRequestURI();
        AntPathMatcher antPathMatcher = new AntPathMatcher();
        boolean isExist = false;
        for (String authUrl : authUrls) {
            boolean match = antPathMatcher.match(authUrl, requestURI);
            if (match) {
                isExist = true;
                break;
            }
        }
        if (isExist) {
            // 获取cookie令牌
            String token = "";
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if(Constant.USER_TOKEN.equals(cookie.getName())) {
                        token = cookie.getValue();
                        break;
                    }
                }
            }
            // 使用cookie令牌，获取redis中保存的用户
            String userStr = stringRedisTemplate.opsForValue().get(Constant.USER_PREFIX + ":" + token);
            if (!StringUtils.isBlank(userStr)) {
                // 保存到本地线程 当前设置的用户数据只作为一个标记，并不是真实数据
                UserEntity userEntity = JSON.parseObject(userStr, UserEntity.class);
                // 同步数据
                threadLocal.set(userEntity);
                return true;
            }
            // 构造响应
            response.setContentType("application/json; charset=utf-8");
            R r = R.error(StatusCodeEnum.NO_AUTH_EXCEPTION.getCode(), StatusCodeEnum.NO_AUTH_EXCEPTION.getMsg());
            response.getWriter().write(JSON.toJSONString(r));
            return false;
        }
        return true;
    }


    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
