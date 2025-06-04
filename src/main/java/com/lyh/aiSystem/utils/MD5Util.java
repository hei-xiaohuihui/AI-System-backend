package com.lyh.aiSystem.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * @author BigHH
 *  MD5加密工具类
 */
@Component
public class MD5Util {

    @Value("${lyh.password.salt}")
    private String SALT;

    /**
     *  MD5加密工具：用于将新注册的用户的密码使用MD5加密，并转为Base64编码返回
     * @param str
     * @return Base64编码后的MD5字符串
     */
    public String getMD5Str(String str) throws NoSuchAlgorithmException {
        // 创建MD5加密对象
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        // 返回加密结果
        return Base64.getEncoder().encodeToString(md5.digest((str + SALT).getBytes()));

//        // 创建MD5加密对象
//        MessageDigest md5 = MessageDigest.getInstance("MD5");
//        byte[] digest = md5.digest((str + SALT).getBytes());
//
//        // 将字节数组转换为十六进制字符串
//        StringBuilder sb = new StringBuilder();
//        for (byte b : digest) {
//            sb.append(String.format("%02x", b & 0xff));
//        }
//        return sb.toString();
    }
}
