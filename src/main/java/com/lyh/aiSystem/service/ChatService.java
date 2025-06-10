package com.lyh.aiSystem.service;

import com.lyh.aiSystem.pojo.vo.ChatMessageVo;
import reactor.core.publisher.Flux;

import java.security.NoSuchAlgorithmException;
import java.util.List;

public interface ChatService {

    Flux<String> handleChat(String sessionId, String message);

//    List<String> getSessionIds();
//
//    List<ChatMessageVo> getHistoryBySessionId(String sessionId);
//
//    void deleteSessionById(String sessionId);
}
