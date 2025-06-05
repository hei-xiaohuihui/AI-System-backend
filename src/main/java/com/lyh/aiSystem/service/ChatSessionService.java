package com.lyh.aiSystem.service;

public interface ChatSessionService {

    void createSessionIfNotExist(String sessionId, Long userId);
}
