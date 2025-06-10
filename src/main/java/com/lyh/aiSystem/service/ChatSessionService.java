package com.lyh.aiSystem.service;

import com.lyh.aiSystem.pojo.vo.ChatMessageVo;

import java.util.List;

public interface ChatSessionService {

    void createSessionIfNotExist(String sessionId, Long userId);
    
    List<String> getSessionIds();

    List<ChatMessageVo> getHistoryBySessionId(String sessionId);

    void deleteSessionById(String sessionId);
}
