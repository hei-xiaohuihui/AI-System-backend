package com.lyh.aiSystem.service.impl;

import com.lyh.aiSystem.enumeration.ExceptionEnum;
import com.lyh.aiSystem.exception.BaseException;
import com.lyh.aiSystem.memory.MySqlChatMemory;
import com.lyh.aiSystem.service.ChatService;
import com.lyh.aiSystem.service.ChatSessionService;
import com.lyh.aiSystem.service.RedisQACacheService;
import com.lyh.aiSystem.utils.UserContextUtil;
import com.lyh.aiSystem.pojo.vo.ChatMessageVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author BigHH
 */
@RequiredArgsConstructor
@Service
@Slf4j
public class ChatServiceImpl implements ChatService {

    // 使用构造函数注入
    private final ChatClient chatClient;

    private final UserContextUtil userContextUtil;

    private final ChatMemory chatMemory;

    private final RedisQACacheService qaCacheService;

    /**
     * 处理会话
     * 
     * @param sessionId
     * @param message
     * @return
     */
    @Override
    public Flux<String> handleChat(String sessionId, String message){
        Long userId = userContextUtil.getUserId();
        // 设置当前用户id到ChatMemory
        ((MySqlChatMemory) chatMemory).setCurrentUserId(userId);

        // 1.先检查缓存中是否有问题对应的答案
        String cacheAnswer = qaCacheService.getAnswerCache(sessionId, message);
        if(cacheAnswer != null) { // 缓存命中
            // 即使用户重复提问，也将用户的提问和回答保存到数据库中
            List<Message> messages = Arrays.asList( // 创建消息列表
                    new UserMessage(message),
                    new AssistantMessage(cacheAnswer)
            );
            // 保存消息列表到数据库中
            chatMemory.add(sessionId, messages);

            // 将缓存的答案按字符分割实现流式返回
            return Flux.fromArray(cacheAnswer.split(""));
        }

        // 2.如果缓存中没有答案，再调用AI模型回答
        StringBuilder fullResponse = new StringBuilder();

        // 调用AI模型并保存回复内容到数据库和缓存中
        return chatClient.prompt()
                .user(message)
                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, sessionId))
                .stream()
                .content()
                .doOnNext(chunk -> fullResponse.append(chunk))
                .doOnComplete(new Runnable() {
                    @Override
                    public void run() {
                        // 在流完成时保存完整的用户提问和ai回复
                        if(!fullResponse.isEmpty()) {
                            qaCacheService.setQACache(sessionId, message, fullResponse.toString());
                        }
                    }
                })
                .doFinally(signalType -> log.debug("Chat completed for session:{}", sessionId));
    }

}
