package com.lyh.aiSystem.config;

import com.lyh.aiSystem.interceptor.AdminJwtInterceptor;
import com.lyh.aiSystem.interceptor.UserJwtInterceptor;
import com.lyh.aiSystem.properties.FileUploadProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author BigHH
 *  WebMvc配置类
 */
@RequiredArgsConstructor
@Configuration
public class WebMVC implements WebMvcConfigurer {

    private final UserJwtInterceptor userJwtInterceptor;

    private final AdminJwtInterceptor adminJwtInterceptor;

    private final FileUploadProperties fileUploadProperties;

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
        // 添加用户端Jwt拦截器
        registry.addInterceptor(userJwtInterceptor)
                .addPathPatterns("/user/**") // 拦截所有/user/**路径
                .excludePathPatterns("/user/auth/register", "/user/auth/login"
//                    ,  "/user/chat/model"
                ); // 不拦截/user/register和/user/login路径


        // 添加管理端Jwt拦截器
        registry.addInterceptor(adminJwtInterceptor)
                .addPathPatterns("/admin/**")
                .excludePathPatterns("/admin/auth/login");
    }

    /**
     *  静态资源映射
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:" + fileUploadProperties.getBasePath() + "/uploads/"); // file表示从本地文件系统映射
    }
}
