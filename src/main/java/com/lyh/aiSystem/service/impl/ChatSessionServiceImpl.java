package com.lyh.aiSystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lyh.aiSystem.entity.ChatSession;
import com.lyh.aiSystem.enumeration.ExceptionEnum;
import com.lyh.aiSystem.exception.BaseException;
import com.lyh.aiSystem.mapper.ChatSessionMapper;
import com.lyh.aiSystem.service.ChatSessionService;
import com.lyh.aiSystem.utils.UserContextUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
}
