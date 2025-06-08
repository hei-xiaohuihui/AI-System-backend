package com.lyh.aiSystem.pojo.vo;

import lombok.Data;

/**
 * @author BigHH
 *  用户详情视图对象
 */
@Data
public class UserDetailVo {

    private String username;

    private String email;

    private String phone;

//    private String avatarUrl;

    private Byte gender;
}
