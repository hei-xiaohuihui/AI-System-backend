package com.lyh.aiSystem.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author BigHH
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("cs_user")
public class User {

    @TableId(value = "id", type = IdType.AUTO) // 可以不加，因为在配置文件中进行了全局配置
    private Long  id;

    private String username;

    private String password;

    private String email;

    private String phone;

    private String avatarUrl;

    private Byte gender;

    private Byte status;

    private LocalDateTime  createdAt;

    private LocalDateTime  updatedAt;
}
