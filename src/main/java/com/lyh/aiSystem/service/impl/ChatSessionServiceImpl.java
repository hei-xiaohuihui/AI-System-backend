package com.lyh.aiSystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lyh.aiSystem.pojo.entity.ChatSession;
import com.lyh.aiSystem.enumeration.ExceptionEnum;
import com.lyh.aiSystem.exception.BaseException;
import com.lyh.aiSystem.mapper.ChatSessionMapper;
import com.lyh.aiSystem.service.ChatSessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author BigHH
 */
@Service
@RequiredArgsConstructor
public class ChatSessionServiceImpl implements ChatSessionService {

    private final ChatSessionMapper sessionMapper;

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
     *  根据用户id获取其所有会话id
     * @param userId
     * @return
     */
    @Override
    public List<String> getSessionIdsByUserId(Long userId) {
        return sessionMapper.selectSessionIds(userId);
    }

    /**
     *  根据会话id获取用户id
     * @param sessionId
     * @return
     */
    @Override
    public Long getUserIdBySessionId(String sessionId) {
        // 更具会话id查询会话
        ChatSession chatSession = sessionMapper.selectOne(new QueryWrapper<ChatSession>().eq("session_id", sessionId));
        if(chatSession == null) {
            throw new BaseException(ExceptionEnum.CHAT_SESSION_NOT_EXIST); // 抛出会话不存在异常
        }
        return chatSession.getUserId();
    }

}
