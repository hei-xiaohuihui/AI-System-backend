package com.lyh.aiSystem.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.management.relation.Role;
import java.time.LocalDateTime;

/**
 * @author BigHH
 *  系统管理员实例类
 */
@Data
@TableName("cs_admin")
public class Admin {

    private Long id;

    private String username;

    private String password;

    private String lecturerName;

    private String lecturerTitle;

//    private Role role;
    private String role;

    private String email;

    private String phone;

    private String avatarUrl;

    // 个人简介: 仅适用于role为LECTURER的管理员用户
    private String bio;

    private Byte gender;

    private Byte status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

//    // 管理员角色枚举
//    public enum Role {
//        SUPER_ADMIN,
//        LECTURER
//    }
}
