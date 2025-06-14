package com.lyh.aiSystem.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author BigHH
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "lyh.milvus")
public class MilvusProperties {

    // 存储在向量数据库元数据中的文档唯一标识（用于按文档进行检索和删除）
    private String saveKey;
}
