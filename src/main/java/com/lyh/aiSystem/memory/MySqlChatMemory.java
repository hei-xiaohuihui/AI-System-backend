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
 *  负责持久化聊天消息到MySql数据库
 */
@Component
@RequiredArgsConstructor
public class MySqlChatMemory implements ChatMemory {

    private final ChatMessageMapper chatMessageMapper;

    private final ChatSessionMapper chatSessionMapper;

    /**
     *  将每条会话消息持久化到MySql数据库（包括用户提问和AI回答）
     * @param conversationId
     * @param messages
     */
    @Override
    public void add(String conversationId, List<Message> messages) {
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

    /**
     *  根据会话id获取其对应的所有会话消息
     * @param conversationId
     * @return
     */
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

    /**
     *  根据会话id删除会话及所有会话消息
     * @param conversationId
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void clear(String conversationId) {
        // 删除会话表中对应的会话记录
        chatSessionMapper.delete(new QueryWrapper<ChatSession>().eq("session_id", conversationId));
        // 删除消息表中对应的消息记录
        chatMessageMapper.delete(new QueryWrapper<ChatMessage>().eq("session_id", conversationId));
    }
}
