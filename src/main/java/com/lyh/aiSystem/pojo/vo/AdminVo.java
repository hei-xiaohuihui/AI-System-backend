package com.lyh.aiSystem.pojo.vo;

import lombok.Data;

/**
 * @author BigHH
 *  管理员账户详情信息视图对象
 */
@Data
public class AdminVo {

    private String username;

    private String lecturerName;

    private String lecturerTitle;

    private String email;

    private String phone;

    private Byte gender;

    private String bio;
}
