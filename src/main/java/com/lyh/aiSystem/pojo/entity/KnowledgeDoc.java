package com.lyh.aiSystem.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author BigHH
 *  超级管理员知识库文档实体类
 */
@Data
@TableName("cs_knowledge_doc")
public class KnowledgeDoc {

    private Long id;

    private String title;

    private String description;

    // 上传的文档资源URL
    private String resourceUrl;

    // 保存在向量数据库中的文档id
    private String ragDocId;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
