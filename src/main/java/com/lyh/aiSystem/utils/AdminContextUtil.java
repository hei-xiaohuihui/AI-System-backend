package com.lyh.aiSystem.utils;

import com.lyh.aiSystem.constant.JwtClaimsConstant;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author BigHH
 *  获取管理员用户上下文信息工具类
 */
@RequiredArgsConstructor
@Component
public class AdminContextUtil {

    private final HttpServletRequest  request;

    private  Claims getClaims() {
        return (Claims) request.getAttribute(JwtClaimsConstant.SYSTEM_CLAIMS);
    }

    /**
     *  获取当前登录的管理员用户id
     * @return
     */
    public Long getAdminId() {
        return getClaims().get(JwtClaimsConstant.ADMIN_ID, Long.class);
    }

    /**
     *  获取当前登录的管理员角色
     * @return
     */
    public String getAdminRole() {
        return getClaims().get(JwtClaimsConstant.ADMIN_ROLE, String.class);
    }
}
