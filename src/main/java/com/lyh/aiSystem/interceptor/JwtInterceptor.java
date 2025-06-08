package com.lyh.aiSystem.interceptor;

import com.lyh.aiSystem.constant.JwtClaimsConstant;
import com.lyh.aiSystem.enumeration.ExceptionEnum;
import com.lyh.aiSystem.exception.BaseException;
import com.lyh.aiSystem.properties.JwtProperties;
import com.lyh.aiSystem.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * @author BigHH
 *  Jwt校验拦截器
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class JwtInterceptor implements HandlerInterceptor {

    private final JwtUtil jwtUtil;

    private final JwtProperties jwtProperties;

//    public JwtInterceptor(JwtUtil jwtUtil, JwtProperties jwtProperties) {
//        this.jwtUtil = jwtUtil;
//        this.jwtProperties = jwtProperties;
//    }

    /**
     *  拦截请求（在请求到达Controller之前执行），进行Jwt校验
     * @param request
     * @param response
     * @param handler
     * @return
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler){
        // 先判断当前请求是否是一个控制器方法（@RequestMapping定义的方法/Controller层方法）
        if(!(handler instanceof HandlerMethod)) {
            return true; // 不是控制器方法，直接放行
        }
        log.debug("开始Jwt校验...");

        // 从请求头中获取Jwt
        String jwt = request.getHeader(jwtProperties.getTokenHead());
        if(jwt == null || jwt.isEmpty()) {
            log.warn("Jwt缺失！");
            throw new BaseException(ExceptionEnum.USER_TOKEN_MISSING); // 用户Token缺失
        }

        log.debug("获取到的Jwt: {}", jwt);
        // 去掉Bearer前缀
        if(jwt.startsWith("Bearer ")) {
            jwt = jwt.substring(7);
        }

        // 校验Jwt
        try {
            Claims claims = jwtUtil.parseJwt(jwt);
            request.setAttribute(JwtClaimsConstant.USER_CLAIMS, claims); // 将Jwt中解析出来的用户数据保存到请求属性中，方便后续使用
            log.debug("Jwt校验成功！");
            return true;
        } catch(ExpiredJwtException e) {
            log.warn("Jwt已过期！");
            throw new BaseException(ExceptionEnum.USER_TOKEN_EXPIRED); // Jwt已过期
        } catch(JwtException e) {
            log.warn("Jwt无效！");
            throw new BaseException(ExceptionEnum.USER_TOKEN_INVALID); // Jwt无效
        }
    }
}
