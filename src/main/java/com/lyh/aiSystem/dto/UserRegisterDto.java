package com.lyh.aiSystem.dto;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author BigHH
 *  用户注册数据传输对象
 */
@Data
public class UserRegisterDto {

    @NotNull(message = "用户名不能为空")
    @Size(min = 5, max = 20, message = "用户名长度必须在3到20之间")
    private String username;

    @NotNull(message = "密码不能为空")
    @Size(min = 6, max = 20, message = "密码长度必须在6到20之间")
    private String password;
}
