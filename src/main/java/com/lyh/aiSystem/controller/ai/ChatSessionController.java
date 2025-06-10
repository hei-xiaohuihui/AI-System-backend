package com.lyh.aiSystem.controller.ai;

import com.lyh.aiSystem.pojo.vo.ChatMessageVo;
import com.lyh.aiSystem.service.ChatSessionService;
import com.lyh.aiSystem.utils.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author BigHH
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/user/session")
public class ChatSessionController {

    private final ChatSessionService chatSessionService;

    /**
     *  获取当前登录用户的所有会话id列表
     * @return
     */
    @GetMapping("/sessionIds")
    public Result getSessionIds() {
        List<String> sessionIdList = chatSessionService.getSessionIds();
        return Result.success(sessionIdList);
    }

    /**
     *  根据会话id获取聊天记录（会话消息列表）
     * @param sessionId
     * @return
     */
    @GetMapping("/history")
    public Result getHistory(@RequestParam("sessionId") String sessionId) {
        List<ChatMessageVo> messageVoList = chatSessionService.getHistoryBySessionId(sessionId);
//        List<ChatMessageVo> messageVoList = chatMemory.get(sessionId).stream().map(msg -> new ChatMessageVo(msg.getMessageType().getValue(), msg.getText())).collect(Collectors.toList());
        return Result.success(messageVoList);
    }

    /**
     *  根据id删除会话及所有会话消息
     */
    @DeleteMapping("/delete")
    public Result deleteSession(@RequestParam("sessionId") String sessionId) {
        chatSessionService.deleteSessionById(sessionId);
        return Result.success();
    }
}
