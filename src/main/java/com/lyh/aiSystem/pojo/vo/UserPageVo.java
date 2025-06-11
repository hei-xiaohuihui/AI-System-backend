package com.lyh.aiSystem.pojo.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author BigHH
 *  超级管理员分页查询用户信息视图对象
 */
@Data
public class UserPageVo {

    private Long id;

    private String username;

    private String email;

    private String phone;

    private Byte gender;

    private Byte status;

    private LocalDateTime createdAt;
}
