package com.lyh.aiSystem.service;

import reactor.core.publisher.Flux;

public interface ChatService {

    Flux<String> handleChat(String sessionId, String message);
}
