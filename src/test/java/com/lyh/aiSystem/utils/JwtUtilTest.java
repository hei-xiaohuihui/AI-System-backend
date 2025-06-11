package com.lyh.aiSystem.utils;

import com.lyh.aiSystem.properties.JwtProperties;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author BigHH
 */
@SpringBootTest
public class JwtUtilTest {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private JwtProperties jwtProperties;

    @Test
    public void testGenerateJwt() {
        String jwt = jwtUtil.generateJwt(jwtProperties.getAdminSecretKey(), null);
        System.out.println(jwt);
    }
}
