package com.lyh.aiSystem.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author BigHH
 *  会话实体类
 */
@Data
@Builder
@TableName("cs_chat_session")
public class ChatSession {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long userId;

    private String sessionId;

    private LocalDateTime createdAt;
}
