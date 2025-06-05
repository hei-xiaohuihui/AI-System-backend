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
import com.lyh.aiSystem.service.ChatSessionService;
import com.lyh.aiSystem.utils.UserContextUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;

import java.util.List;


/**
 * @author BigHH
 */
@RequiredArgsConstructor
@Service
public class ChatServiceImpl implements ChatService {

    // 使用构造函数注入
    private final ChatClient chatClient;

    private final UserContextUtil userContextUtil;

    private final ChatSessionService chatSessionService;

    /**
     *  处理会话
     * @param sessionId
     * @param message
     * @return
     */
//    @Transactional(rollbackFor = Exception.class)
    @Override
    public Flux<String> handleChat(String sessionId, String message) {
        // 创建会话
        chatSessionService.createSessionIfNotExist(sessionId, userContextUtil.getUserId());

        // 调用AI模型并保存AI回复信息
        return chatClient.prompt()
                .user(message)
                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, sessionId)) // 设置会话id
                .stream()
                .content();
    }

    /**
     *  获取当前登录用户的所有会话id列表
     * @return
     */
    @Override
    public List<String> getSessionIds() {
        return chatSessionService.getSessionIdsByUserId(userContextUtil.getUserId());
    }


}
