package com.lyh.aiSystem.memory;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lyh.aiSystem.constant.ChatRoleConstant;
import com.lyh.aiSystem.entity.ChatMessage;
import com.lyh.aiSystem.entity.ChatSession;
import com.lyh.aiSystem.enumeration.ExceptionEnum;
import com.lyh.aiSystem.exception.BaseException;
import com.lyh.aiSystem.mapper.ChatMessageMapper;
import com.lyh.aiSystem.mapper.ChatSessionMapper;
import com.lyh.aiSystem.service.ChatService;
import com.lyh.aiSystem.service.ChatSessionService;
import com.lyh.aiSystem.utils.UserContextUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author BigHH
 */
@Component
@RequiredArgsConstructor
public class MySqlChatMemory implements ChatMemory {

    private final ChatMessageMapper chatMessageMapper;

    private final ChatSessionMapper chatSessionMapper;

//    private final ChatSessionService  chatSessionService;

//    private final UserContextUtil userContextUtil;

//    @Transactional(rollbackFor = Exception.class)
    @Override
    public void add(String conversationId, List<Message> messages) {
        // 创建会话表
//        chatSessionService.createSessionIfNotExist(conversationId, userContextUtil.getUserId());

        // 处理会话消息，使用ChatMessage对象的集合保存
        List<ChatMessage> chatMessageList = messages.stream().map(msg -> {
            ChatMessage chatMessage = ChatMessage.builder()
                    .sessionId(conversationId)
                    .message(msg.getText())
                    .build();

            if (msg instanceof UserMessage) { // 用户消息
                chatMessage.setSender(ChatRoleConstant.CHAT_ROLE_USER);
            } else if (msg instanceof AssistantMessage) { // AI消息
                chatMessage.setSender(ChatRoleConstant.CHAT_ROLE_ASSISTANT);
            }
            return chatMessage;
        }).collect(Collectors.toList());

        // 插入数据库
        chatMessageMapper.insert(chatMessageList);
    }

    @Override
    public List<Message> get(String conversationId) {
        List<ChatMessage> messages = chatMessageMapper.getMessagesBySessionId(conversationId);
        return messages.stream().map(msg -> {
            if(msg.getSender().equals(ChatRoleConstant.CHAT_ROLE_USER)) { // 用户消息
                return new UserMessage(msg.getMessage());
            } else if(msg.getSender().equals(ChatRoleConstant.CHAT_ROLE_ASSISTANT)) { // AI消息
                return new AssistantMessage(msg.getMessage());
            } else {
                throw new BaseException(ExceptionEnum.CHAT_ROLE_UNKNOWN); // 未知聊天角色抛出异常
            }
        }).collect(Collectors.toList());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void clear(String conversationId) {
        // 删除会话表中对应的会话记录
        chatSessionMapper.delete(new QueryWrapper<ChatSession>().eq("session_id", conversationId));
        // 删除消息表中对应的消息记录
        chatMessageMapper.delete(new QueryWrapper<ChatMessage>().eq("session_id", conversationId));
    }
}
