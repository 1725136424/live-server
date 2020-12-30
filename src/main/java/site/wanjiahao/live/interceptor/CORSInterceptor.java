package site.wanjiahao.live.interceptor;

import org.apache.http.HttpStatus;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import site.wanjiahao.live.constant.Constant;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CORSInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        response.setHeader("Access-Control-Allow-Origin", Constant.SCHEMAS + Constant.DOMAIN + ":" + Constant.PORT);
        response.setHeader("Access-Control-Allow-Methods", "*");
        // 设置跨域请求头
        response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
        // 允许携带凭证数据 Cookie
        // 前端设置withCredentials = true
        // Access-Control-Allow-Origin 不能设置为* 需要指定跨域的域
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Max-Age", Constant.MAX_AGE + "");
        // 预检请求直接返回200，当是options请求的时候，不会携带凭证数据，也就是Cookie，导致登录拦截器出问题，直接返回错误状态码
        if ("OPTIONS".equals(request.getMethod())) {
            response.setStatus(HttpStatus.SC_OK);
            return false;
        }
        return true;
    }

    /**
     * 渲染视图之后，注意已经执行了控制器的方法，所以在此设置跨域是无效的
     * 这个时候可对ModelAndView进行操作
     * @param request
     * @param response
     * @param handler
     * @param modelAndView
     * @throws Exception
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    /**
     * 视图渲染完毕执行，可以用于清理数据
     * @param request
     * @param response
     * @param handler
     * @param ex
     * @throws Exception
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
