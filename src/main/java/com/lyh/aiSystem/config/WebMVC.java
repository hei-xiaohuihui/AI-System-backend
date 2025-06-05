package com.lyh.aiSystem.config;

import com.lyh.aiSystem.interceptor.JwtInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author BigHH
 *  WebMvc配置类
 */
@Configuration
public class WebMVC implements WebMvcConfigurer {

    private final JwtInterceptor jwtInterceptor;

    public WebMVC(JwtInterceptor jwtInterceptor) {
        this.jwtInterceptor = jwtInterceptor;
    }

    /**
     *  跨域配置
     * @param registry
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 拦截所有请求路径
                .allowedOrigins("*") // 允许的请求源
                .allowedMethods("*") // 允许的请求方法
                .allowedHeaders("*"); // 允许的请求头
    }

    /**
     *  拦截器配置
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor) // 添加拦截器
                .addPathPatterns("/user/**") // 拦截所有/user/**路径
                .excludePathPatterns("/user/auth/register", "/user/auth/login"
//                    ,  "/user/chat/model"
                ); // 不拦截/user/register和/user/login路径
    }
}
