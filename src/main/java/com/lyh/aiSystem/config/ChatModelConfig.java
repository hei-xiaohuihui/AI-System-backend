package com.lyh.aiSystem.config;

import com.lyh.aiSystem.tool.DataTimeTools;
import com.lyh.aiSystem.tool.LectureTools;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
//import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiEmbeddingModel;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;


/**
 * @author BigHH
 *         模型配置类
 */
@RequiredArgsConstructor
@Configuration
public class ChatModelConfig {

    private final ChatMemory chatMemory;
    // private final ChatMemory MysqlChatMemory;

    // private final VectorStore vectorStore;
//     @Bean
//     public VectorStore vectorStore(OpenAiEmbeddingModel embeddingModel) {
//        return SimpleVectorStore.builder(embeddingModel).build();
//     }

    // 引入系统提示词文件
    @Value("classpath:prompt/RAG.txt")
    private Resource ragPromptResource;

    /**
     * 创建ChatClient
     * @param model
     * @return
     */
    @Bean
    public ChatClient chatClient(OpenAiChatModel model, VectorStore vectorStore , DataTimeTools dataTimeTools, LectureTools lectureTools) {
        return ChatClient.builder(model)
                .defaultSystem(ragPromptResource) // 设置系统提示词
                .defaultAdvisors(
                        new SimpleLoggerAdvisor(), // 配置日志Advisor
                        MessageChatMemoryAdvisor.builder(chatMemory).build(), // 配置会话记忆Advisor，它会自动调用重写的ChatMemory的add方法，将消息持久化到数据库中
                        QuestionAnswerAdvisor // 配置向量数据库Advisor
                                .builder(vectorStore)
                                .searchRequest( // 搜索向量数据库的参数配置
                                        SearchRequest.builder()
                                        .similarityThreshold(0.5f)
                                        .topK(2)
                                        .build())
                                .build()
                )
                .defaultTools(dataTimeTools, lectureTools) // 配置工具，实现工具的调用
                .build();
    }
}
