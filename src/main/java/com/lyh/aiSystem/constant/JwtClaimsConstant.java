package com.lyh.aiSystem.constant;

/**
 * @author BigHH
 *  Jwt自定义数据Key常量类
 */
public class JwtClaimsConstant {

    // Claim
    public static final String SYSTEM_CLAIMS = "systemClaims";

    /**
     *  普通用户相关
     */
    // 用户主键id
    public static final String USER_ID = "userId";

    // 用户名
    public static final String USER_NAME = "userName";

    /**
     *  管理员相关
     */
    // 管理员主键id
    public static final String ADMIN_ID = "adminId";

    // 管理员角色
    public static final String ADMIN_ROLE = "adminRole";

    // 管理员名
    public static final String ADMIN_NAME = "adminName";
}
