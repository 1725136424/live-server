package site.wanjiahao.live.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import site.wanjiahao.live.interceptor.CORSInterceptor;
import site.wanjiahao.live.interceptor.LoginInterceptor;

@Configuration
public class WebMVCConfig implements WebMvcConfigurer {

    @Autowired
    private LoginInterceptor loginInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 配置跨域
        registry.addInterceptor(new CORSInterceptor()).addPathPatterns("/**");
        // 配置登录拦截
        registry.addInterceptor(loginInterceptor).addPathPatterns("/**");
    }

    /**
     * resp.addHeader("Access-Control-Max-Age", "0")，表示每次异步请求都发起预检请求，也就是说，发送两次请求
     * esp.addHeader("Access-Control-Max-Age", "1800")，表示每隔30min才发送预检请求， 在30min之内，需要发送预检请求
     * @param registry
     */
   /* @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry
                .addMapping("/**")
                .allowedOrigins("*")
                .allowedHeaders("*")
                .allowedMethods("POST", "GET", "PUT", "OPTIONS", "DELETE")
                .allowedHeaders("*")
                .maxAge(3600);
    }*/
}
