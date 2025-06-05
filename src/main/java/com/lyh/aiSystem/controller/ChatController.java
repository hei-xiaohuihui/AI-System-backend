package com.lyh.aiSystem.controller;

import com.lyh.aiSystem.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import static org.apache.coyote.http11.Constants.a;

/**
 * @author BigHH
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/user/chat")
public class ChatController {

//    private final ChatClient chatClient;
//
//    // 使用构造函数注入
//    public ChatController(ChatClient chatClient) {
//        this.chatClient = chatClient;
//    }
    private final ChatService chatService;

    /**
     *  用于与AI聊天接口
     * @param sessionId
     * @param message
     * @return
     */
//    @GetMapping(value ="/model", produces ="text/html; charset=utf-8")
    @GetMapping(value ="/model", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> chat(@RequestParam("sessionId") String sessionId,
                             @RequestParam("message") String message) {
//        return chatClient.prompt()
//                .user(message)
//                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID,  sessionId)) // 设置会话ID
//                .stream()
//                .content();
        return chatService.handleChat(sessionId, message);
    }
}
