package com.lyh.aiSystem.utils;

import com.lyh.aiSystem.properties.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.Map;

/**
 * @author BigHH
 *  生成Jwt工具类
 */
//@RequiredArgsConstructor
@Component
public class JwtUtil {

//    @Value("${lyh.jwt.secret-key}")
//    private String secretKey;
//
//    @Value("${lyh.jwt.expire-time}")
//    private long expireTime;
    private final JwtProperties jwtProperties;
    public JwtUtil(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }

    // 生成hmac类型密钥
    private Key jwtKey;
    @PostConstruct
    public void init() {
        jwtKey = Keys.hmacShaKeyFor(jwtProperties.getSecretKey().getBytes(StandardCharsets.UTF_8));
    }

//    Key jwtKey = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));// 这段代码在字段声明时被执行，此时secretKey还没有被@Value注入，导致NullPointerException

    /**
     *  生成Jwt
     * @param dataMap
     * @return
     */
    public String generateJwt(Map<String, Object> dataMap) {
        return Jwts.builder()
                .signWith(jwtKey, SignatureAlgorithm.HS256) // 设置签名算法和密钥
                .addClaims(dataMap) // 添加自定义数据
                .setExpiration(new Date(System.currentTimeMillis() + jwtProperties.getExpireTime())) // 设置过期时间
                .compact();
    }

    /**
     *  解析Jwt
     * @param jwt
     * @return
     */
    public Claims parseJwt(String jwt) {
        return Jwts.parserBuilder()
                .setSigningKey(jwtKey) // 设置签名密钥
                .build() // 构建parser解析器
                .parseClaimsJws(jwt) // 解析Jwt
                .getBody(); // 获取payload中的Claims
    }
}
