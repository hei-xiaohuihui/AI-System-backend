package com.lyh.aiSystem.config;

import io.milvus.client.MilvusServiceClient;
import io.milvus.param.ConnectParam;
import io.milvus.param.IndexType;
import io.milvus.param.MetricType;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.embedding.TokenCountBatchingStrategy;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.milvus.MilvusVectorStore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author BigHH
 */
@Configuration
public class MilvusVectorStoreConfig {

    /**
     * Milvus连接参数配置
     */
    @Bean
    public MilvusServiceClient milvusClient() {
        return new MilvusServiceClient(ConnectParam.newBuilder()
                .withAuthorization("root", "qaz147852")
                .withHost("localhost")
                .withPort(19530)
                .build());
    }

    /**
     *  向量数据库配置
     * @param milvusClient
     * @param embeddingModel 使用@Qualifier("openAiEmbeddingModel")指定使用的Embedding模型，避免Bean冲突
     * @return
     */
    @Bean
    public VectorStore vectorStore(MilvusServiceClient milvusClient, @Qualifier("openAiEmbeddingModel") EmbeddingModel embeddingModel) {
        return MilvusVectorStore.builder(milvusClient, embeddingModel)
                .collectionName("cs_doc")
                .databaseName("default")
                .indexType(IndexType.IVF_FLAT)
                .metricType(MetricType.COSINE)
                .batchingStrategy(new TokenCountBatchingStrategy())
                .initializeSchema(true)
                .build();
    }
}
