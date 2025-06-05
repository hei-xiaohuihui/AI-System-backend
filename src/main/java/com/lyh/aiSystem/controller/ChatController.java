package com.lyh.aiSystem.controller;

import com.lyh.aiSystem.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

/**
 * @author BigHH
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/user/chat")
public class ChatController {

    private final ChatService chatService;

    /**
     *  用于与AI聊天接口
     * @param sessionId
     * @param message
     * @return
     */
    @GetMapping(value ="/model", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> chat(@RequestParam("sessionId") String sessionId,
                             @RequestParam("message") String message) {
        return chatService.handleChat(sessionId, message);
    }


}
