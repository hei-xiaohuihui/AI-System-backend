package com.lyh.aiSystem.pojo.dto;

import lombok.Data;

import jakarta.validation.constraints.Size;

/**
 * @author BigHH
 *  管理员更新信息数据传输对象
 */
@Data
public class AdminUpdateDto {

    private Long id;

    @Size(min = 5, max = 20, message = "用户名长度必须在5到20之间")
    private String username;

    @Size(min = 6, max = 20, message = "密码长度必须在6到20之间")
    private String password;

    private String lecturerName;

    private String lecturerTitle;

    private String email;

    private String phone;

    private Byte gender;

    private String bio;
}
