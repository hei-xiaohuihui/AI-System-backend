package com.lyh.aiSystem.pojo.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * @author BigHH
 *  创建普通管理员数据传输对象
 */
@Data
public class AdminCreateDto {

    @NotBlank
    @Size(min = 5, max = 20, message = "用户名长度必须在5到20之间")
    private String username;

    @NotBlank
    @Size(min = 6, max = 20, message = "密码长度必须在6到20之间")
    private String password;

    private String email;

    private String phone;

    private Byte gender;
}
