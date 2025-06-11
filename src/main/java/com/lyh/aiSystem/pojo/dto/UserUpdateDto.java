package com.lyh.aiSystem.pojo.dto;

import lombok.Data;

import jakarta.validation.constraints.Size;

/**
 * @author BigHH
 *  用户修改信息数据传输对象
 */
@Data
public class UserUpdateDto {

    @Size(min = 6, max = 20, message = "密码长度必须在6到20之间")
    private String password;

    private String email;

    private String phone;

//    private String avatarUrl;

    private Byte gender;

//    private Byte status;
}
