package com.lyh.aiSystem.utils;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.security.NoSuchAlgorithmException;

/**
 * @author BigHH
 */
@SpringBootTest
public class MD5UtilTest {

    @Autowired
    private MD5Util md5Util;

    @Test
    public void testGetMDStr() throws NoSuchAlgorithmException {
        String password = "admin";
        String md5Str = md5Util.getMD5Str(password);
        System.out.println(md5Str);
    }
}
