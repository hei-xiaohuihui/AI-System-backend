package com.lyh.aiSystem.controller;

import com.lyh.aiSystem.service.ChatService;
import com.lyh.aiSystem.utils.Result;
import com.lyh.aiSystem.utils.UserContextUtil;
import com.lyh.aiSystem.vo.ChatMessageVo;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.Message;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author BigHH
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/user/chat")
public class ChatController {

    private final ChatService chatService;

//    private final ChatMemory chatMemory;

//    private final UserContextUtil userContextUtil;

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
        List<ChatMessageVo> messageVoList = chatService.getHistoryBySessionId(sessionId);
//        List<ChatMessageVo> messageVoList = chatMemory.get(sessionId).stream().map(msg -> new ChatMessageVo(msg.getMessageType().getValue(), msg.getText())).collect(Collectors.toList());
        return Result.success(messageVoList);
    }

    /**
     *  根据id删除会话及所有会话消息
     */
    @DeleteMapping("/delete")
    public Result deleteSession(@RequestParam("sessionId") String sessionId) {
        chatService.deleteSessionById(sessionId);
//        chatMemory.clear(sessionId);
        return Result.success();
    }
}
