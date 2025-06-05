package com.lyh.aiSystem.utils;

import com.lyh.aiSystem.constant.JwtClaimsConstant;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author BigHH
 *  获取用户上下文信息工具类
 */
@RequiredArgsConstructor
@Component
public class UserContextUtil {

    private final HttpServletRequest  request;

    private Claims getClaims() {
        return (Claims) request.getAttribute(JwtClaimsConstant.USER_CLAIMS);
    }

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
    public String getUserName() {
        return this.getClaims().get(JwtClaimsConstant.USER_NAME, String.class);
    }
}
