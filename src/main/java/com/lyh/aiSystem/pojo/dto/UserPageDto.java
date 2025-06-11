package com.lyh.aiSystem.pojo.dto;

import lombok.Data;

/**
 * @author BigHH
 *  超级管理员查询用户信息分页传输对象
 */
@Data
public class UserPageDto {

    private int pageNum = 1;

    private int pageSize = 10;

    // 支持对username、email、phone进行模糊查询
    private String username;

    private String email;

    private String phone;

    // 支持对status的条件查询
    private Byte status;
}
