package com.lyh.aiSystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lyh.aiSystem.pojo.entity.ChatSession;
import com.lyh.aiSystem.enumeration.ExceptionEnum;
import com.lyh.aiSystem.exception.BaseException;
import com.lyh.aiSystem.mapper.ChatSessionMapper;
import com.lyh.aiSystem.pojo.vo.ChatMessageVo;
import com.lyh.aiSystem.service.ChatSessionService;
import com.lyh.aiSystem.service.RedisQACacheService;
import com.lyh.aiSystem.utils.UserContextUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author BigHH
 */
@Service
//@RequiredArgsConstructor
public class ChatSessionServiceImpl implements ChatSessionService {

    private final ChatSessionMapper sessionMapper;

    private final UserContextUtil userContextUtil;

    private final RedisQACacheService qaCacheService;

    private final ChatMemory  chatMemory;

    public ChatSessionServiceImpl(ChatSessionMapper sessionMapper,
                                  UserContextUtil userContextUtil,
                                  RedisQACacheService qaCacheService,
                                  @Lazy ChatMemory chatMemory) { // 使用懒加载解决循环依赖问题
        this.sessionMapper = sessionMapper;
        this.userContextUtil = userContextUtil;
        this.qaCacheService = qaCacheService;
        this.chatMemory = chatMemory;
    }

    /**
     *  创建会话——会话表
     * @param sessionId
     */
    @Override
    public void createSessionIfNotExist(String sessionId, Long userId) {
        // 根据sessionId判断会话是否存在，如果会话不存在，则创建
        if(sessionMapper.selectCount(new QueryWrapper<ChatSession>().eq("session_id", sessionId)) == 0) {
            ChatSession chatSession = ChatSession.builder()
                    .userId(userId)
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
     * 获取当前登录用户的所有会话id列表
     * @return
     */
    @Override
    public List<String> getSessionIds() {
        return sessionMapper.selectSessionIds(userContextUtil.getUserId());
    }

    /**
     * 根据会话id获取会话消息记录
     * @param sessionId
     * @return
     */
    @Override
    public List<ChatMessageVo> getHistoryBySessionId(String sessionId) {
        // 检查会话是否输入当前用户
        checkSessionBelongToCurrentUser(sessionId);
        // 获取会话消息记录
        return chatMemory.get(sessionId).stream()
                .map(msg -> new ChatMessageVo(msg.getMessageType().getValue(), msg.getText()))
                .collect(Collectors.toList());
    }

    /**
     * 根据会话id删除会话
     * @param sessionId
     */
    @Override
    public void deleteSessionById(String sessionId) {
        // 检查会话是否输入当前用户
        checkSessionBelongToCurrentUser(sessionId);

        // 删除会话包含的所有问题的答案、计数缓存
        qaCacheService.deleteCacheBySessionId(sessionId);

        // 删除会话及所有会话消息
        chatMemory.clear(sessionId);
    }

    /**
     * 检查会话是否输入当前用户的辅助方法
     */
    private void checkSessionBelongToCurrentUser(String sessionId) {
        // 根据会话id查询会话
        ChatSession chatSession = sessionMapper.selectOne(new QueryWrapper<ChatSession>().eq("session_id", sessionId));
        if(chatSession == null) {
            throw new BaseException(ExceptionEnum.CHAT_SESSION_NOT_EXIST); // 抛出会话不存在异常
        }

        if (!chatSession.getUserId().equals(userContextUtil.getUserId())) {
            throw new BaseException(ExceptionEnum.CHAT_HISTORY_NOT_BELONG_TO_CURRENT_USER); // 抛出会话记录不属于当前用户异常
        }
    }

}
