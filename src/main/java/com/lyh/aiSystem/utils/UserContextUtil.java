package com.lyh.aiSystem.utils;

import com.lyh.aiSystem.constant.JwtClaimsConstant;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * @author BigHH
 *  获取普通用户上下文信息工具类
 */
@RequiredArgsConstructor
@Component
public class UserContextUtil {

    private final HttpServletRequest  request;

    private Claims getClaims() {
//        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        return (Claims) request.getAttribute(JwtClaimsConstant.SYSTEM_CLAIMS);
    }

//    private HttpServletRequest getCurrentRequest() {
//        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
//        RequestContextHolder.setRequestAttributes(attributes,true); // 设置为true，表示允许在非Web请求上下文中使用
//        if (attributes == null) {
//            throw new IllegalStateException("无法获取当前请求，请确保在 Web 请求上下文中调用该方法");
//        }
//        return attributes.getRequest();
//    }
//
//    private Claims getClaims() {
//        HttpServletRequest request = getCurrentRequest();
//        return (Claims) request.getAttribute(JwtClaimsConstant.USER_CLAIMS);
//    }

    /**
     *  获取当前登录用户的id
     * @return
     */
    public Long getUserId() {
        return this.getClaims().get(JwtClaimsConstant.USER_ID, Long.class);
    }

    /**
     *  获取当前登录用户的username
     * @return
     */
//    public String getUserName() {
//        return this.getClaims().get(JwtClaimsConstant.USER_NAME, String.class);
//    }
}
