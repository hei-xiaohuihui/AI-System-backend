package com.lyh.aiSystem.pojo.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import jakarta.validation.constraints.Size;

/**
 * @author BigHH
 *  用户注册数据传输对象
 */
@Data
public class UserRegisterDto {

    @NotBlank(message = "用户名不能为空")
    @Size(min = 5, max = 20, message = "用户名长度必须在3到20之间")
    private String username;

    @NotBlank(message = "密码不能为空")
    @Size(min = 6, max = 20, message = "密码长度必须在6到20之间")
    private String password;
}
