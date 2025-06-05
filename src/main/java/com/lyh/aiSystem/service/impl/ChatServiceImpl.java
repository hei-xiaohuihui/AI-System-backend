package com.lyh.aiSystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lyh.aiSystem.constant.ChatRoleConstant;
import com.lyh.aiSystem.constant.JwtClaimsConstant;
import com.lyh.aiSystem.entity.ChatMessage;
import com.lyh.aiSystem.entity.ChatSession;
import com.lyh.aiSystem.enumeration.ExceptionEnum;
import com.lyh.aiSystem.exception.BaseException;
import com.lyh.aiSystem.mapper.ChatMessageMapper;
import com.lyh.aiSystem.mapper.ChatSessionMapper;
import com.lyh.aiSystem.service.ChatService;
import com.lyh.aiSystem.utils.UserContextUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;


/**
 * @author BigHH
 */
@RequiredArgsConstructor
@Service
public class ChatServiceImpl implements ChatService {

    // 使用构造函数注入
    private final ChatSessionMapper sessionMapper;

    private final ChatMessageMapper messageMapper;

    private final UserContextUtil userContextUtil;

    private final ChatClient chatClient;

    /**
     *  处理会话
     * @param sessionId
     * @param message
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Flux<String> handleChat(String sessionId, String message) {
        // 创建会话
        this.createSessionIfNotExist(sessionId);

        // 保存用户信息
        this.saveMessage(sessionId, ChatRoleConstant.CHAT_ROLE_USER, message);

        // 调用AI模型并保存AI回复信息
        return chatClient.prompt()
                .user(message)
                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, sessionId)) // 设置会话id
                .stream()
                .content()
                .doOnNext(aiResponse -> this.saveMessage(sessionId, ChatRoleConstant.CHAT_ROLE_AI, aiResponse)); // 保存AI回复信息
    }

    /**
     *  创建会话——会话表
     * @param sessionId
     */
    public void createSessionIfNotExist(String sessionId) {
        // 根据sessionId判断会话是否存在，如果会话不存在，则创建
        if(sessionMapper.selectCount(new QueryWrapper<ChatSession>().eq("session_id", sessionId)) == 0) {
            ChatSession chatSession = ChatSession.builder()
                    .userId(userContextUtil.getUserId())
                    .sessionId(sessionId)
                    .build();
            // 插入会话表
            int insertResult = sessionMapper.insert(chatSession);
            if(insertResult == 0) {
                throw new BaseException(ExceptionEnum.DB_INSERT_ERROR); // 抛出数据库插入失败异常
            }
        }
    }

    /**
     *  保存会话消息——会话消息表
     * @param sessionId
     * @param sender
     * @param message
     */
    public void saveMessage(String sessionId, String sender, String message) {
        ChatMessage chatMessage = ChatMessage.builder()
                .sessionId(sessionId)
                .sender(sender)
                .message(message)
                .build();
        // 插入会话消息表
        int insertResult = messageMapper.insert(chatMessage);
        if(insertResult == 0) {
            throw new BaseException(ExceptionEnum.DB_INSERT_ERROR); // 抛出数据库插入失败异常
        }
    }
}
