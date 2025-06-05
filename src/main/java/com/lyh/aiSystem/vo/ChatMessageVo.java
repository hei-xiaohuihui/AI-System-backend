package com.lyh.aiSystem.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author BigHH
 *  聊天消息视图对象
 */
@Data
@AllArgsConstructor
public class ChatMessageVo {

    private String sender;

    private String message;
}
