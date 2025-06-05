package com.lyh.aiSystem.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author BigHH
 *  模型配置类
 */
@Configuration
public class ChatModelConfig {

    // 创建一个默认的消息窗口会话记忆容器
    MessageWindowChatMemory memory = MessageWindowChatMemory.builder()
            .maxMessages(25) // 最多记录的上下文消息数
//            .chatMemoryRepository()  // todo 做消息的持久化存储
            .build();

    /**
     *  创建ChatClient
     * @param model
     * @return
     */
    @Bean
    public ChatClient chatClient(OllamaChatModel model) {
        return ChatClient.builder(model)
//                .defaultSystem("你的名字叫小团团，你是一个热心可爱的AI智能助手。") // 设置系统提示词
                .defaultAdvisors(new SimpleLoggerAdvisor(), // 配置日志Advisor
                        MessageChatMemoryAdvisor.builder(memory).build()) //  配置会话记忆Advisor
                .build();
    }
}
