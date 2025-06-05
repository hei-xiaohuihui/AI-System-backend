package com.lyh.aiSystem.service;

import reactor.core.publisher.Flux;

import java.util.List;

public interface ChatService {

    Flux<String> handleChat(String sessionId, String message);

    List<String> getSessionIds();
}
