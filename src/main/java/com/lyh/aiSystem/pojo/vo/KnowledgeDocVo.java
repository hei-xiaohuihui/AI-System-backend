package com.lyh.aiSystem.pojo.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author BigHH
 *  知识库文档视图对象
 */
@Data
public class KnowledgeDocVo {

    private Long id;

    private String title;

    private String description;

    private String resourceUrl;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
