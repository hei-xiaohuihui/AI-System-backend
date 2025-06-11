package com.lyh.aiSystem.pojo.dto;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * @author BigHH
 *  管理员登录数据传输对象
 */
@Data
public class AdminLoginDto {

    @NotBlank
    @Size(min = 5, max = 20, message = "用户名长度在5-20之间")
    private String username;

    @NotBlank
    @Size(min = 6, max = 20, message = "密码长度在6-20之间")
    private String password;
}
