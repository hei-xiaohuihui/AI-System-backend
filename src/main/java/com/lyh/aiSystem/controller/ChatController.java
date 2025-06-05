package com.lyh.aiSystem.controller;

import com.lyh.aiSystem.service.ChatService;
import com.lyh.aiSystem.utils.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.Message;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.util.List;

/**
 * @author BigHH
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/user/chat")
public class ChatController {

    private final ChatService chatService;

    private final ChatMemory chatMemory;

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

    /**
     *  获取当前登录用户的所有会话id列表
     * @return
     */
    @GetMapping("/sessionIds")
    public Result getSessionIds() {
        List<String> sessionIdList = chatService.getSessionIds();
        return Result.success(sessionIdList);
    }

    /**
     *  根据会话id获取聊天记录（会话消息列表）
     * @param sessionId
     * @return
     */
    @GetMapping("/history")
    public Result getHistory(@RequestParam("sessionId") String sessionId) {
        List<Message> messageList = chatMemory.get(sessionId);
        return Result.success(messageList);
    }

    /**
     *  根据id删除会话及所有会话消息
     */
    @DeleteMapping("/delete")
    public Result deleteSession(@RequestParam("sessionId") String sessionId) {
        chatMemory.clear(sessionId);
        return Result.success();
    }
}
