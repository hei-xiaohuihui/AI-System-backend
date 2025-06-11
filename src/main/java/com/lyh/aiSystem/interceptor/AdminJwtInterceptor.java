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
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * @author BigHH
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class AdminJwtInterceptor implements HandlerInterceptor {

    private final JwtProperties jwtProperties;

    private final JwtUtil jwtUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if(!(handler instanceof HandlerMethod)) {
            return true;
        }
        log.debug("Admin: 开始Jwt校验...");

        // 从请求头中获取Jwt
        String jwt = request.getHeader(jwtProperties.getTokenHead());
        if(jwt == null || jwt.isEmpty()) {
            log.warn("Admin: Jwt缺失！");
            throw new BaseException(ExceptionEnum.USER_TOKEN_MISSING); // Token缺失
        }

        log.debug("Admin: 获取到的Jwt: {}", jwt);
        // 去掉Bearer前缀
        if(jwt.startsWith("Bearer ")) {
            jwt = jwt.substring(7);
        }

        try {
            // 校验Jwt
            Claims claims = jwtUtil.parseJwt(jwtProperties.getAdminSecretKey(), jwt);
            // 将Jwt中解析出的用于信息保存到请求属性中，方便后续使用
            request.setAttribute(JwtClaimsConstant.SYSTEM_CLAIMS, claims);
            log.debug("Admin: Jwt校验成功！");
            return true;
        } catch (ExpiredJwtException e) {
            log.warn("Admin: Jwt已过期！");
            throw new BaseException(ExceptionEnum.USER_TOKEN_EXPIRED);
        } catch (JwtException e) {
            log.warn("Admin: Jwt无效！");
            throw new BaseException(ExceptionEnum.USER_TOKEN_INVALID);
        }
    }
}
