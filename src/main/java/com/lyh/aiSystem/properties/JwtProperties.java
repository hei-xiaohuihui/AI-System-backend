package com.lyh.aiSystem.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author BigHH
 *  Jwt配置读取类
 */
@Configuration
@ConfigurationProperties(prefix = "lyh.jwt")
@Data
public class JwtProperties {

    // 用户端Jwt密钥
    private String userSecretKey;

    // 管理端Jwt密钥
    private String adminSecretKey;

    // Jwt过期时间
    private long expireTime;

    // 保存Jwt的字段名称
    private String tokenHead;
}
