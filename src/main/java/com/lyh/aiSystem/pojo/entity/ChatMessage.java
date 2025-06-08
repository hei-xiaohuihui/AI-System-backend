package com.lyh.aiSystem.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author BigHH
 *  会话消息实体类
 */
@Data
@Builder
@TableName("cs_chat_message")
public class ChatMessage {

    private Long id;

    private String sessionId;

    // 发送者: "user" or "ai"
    private String sender;

    private String message;

    private LocalDateTime createdAt;
}
