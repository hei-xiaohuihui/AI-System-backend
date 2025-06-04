package com.lyh.aiSystem.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author BigHH
 *  用户登录数据传输对象
 */
@Data
public class UserLoginDto {

    @NotNull
    @Size(min = 5, max = 20, message = "用户名长度在5-20之间")
    private String username;

    @NotNull
    @Size(min = 6, max = 20, message = "密码长度在6-20之间")
    private String password;
}
