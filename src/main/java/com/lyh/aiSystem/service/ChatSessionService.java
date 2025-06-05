package com.lyh.aiSystem.service;

import java.util.List;

public interface ChatSessionService {

    void createSessionIfNotExist(String sessionId, Long userId);

    List<String> getSessionIdsByUserId(Long userId);

    Long  getUserIdBySessionId(String sessionId);
}
