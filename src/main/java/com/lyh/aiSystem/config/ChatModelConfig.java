package com.lyh.aiSystem.config;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author BigHH
 *  模型配置类
 */
@RequiredArgsConstructor
@Configuration
public class ChatModelConfig {

    private final ChatMemory chatMemory;

    /**
     *  创建ChatClient
     * @param model
     * @return
     */
    @Bean
    public ChatClient chatClient(OllamaChatModel model) {
        return ChatClient.builder(model)
//                .defaultSystem("你的名字叫小团团，你是一个热心可爱的AI智能助手。") // 设置系统提示词
                .defaultAdvisors(
                        new SimpleLoggerAdvisor(), // 配置日志Advisor
                        MessageChatMemoryAdvisor.builder(chatMemory).build() //  配置会话记忆Advisor，它会自动调用重写的ChatMemory的add方法，将消息持久化到数据库中
                )
                .build();
    }
}
