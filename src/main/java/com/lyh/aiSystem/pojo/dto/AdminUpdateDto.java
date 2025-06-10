package com.lyh.aiSystem.pojo.dto;

import lombok.Data;

import javax.validation.constraints.Size;

/**
 * @author BigHH
 *  管理员更新信息数据传输对象
 */
@Data
public class AdminUpdateDto {

    @Size(min = 6, max = 20, message = "密码长度必须在6到20之间")
    private String password;

    private String email;

    private String phone;

    private Byte gender;
}
