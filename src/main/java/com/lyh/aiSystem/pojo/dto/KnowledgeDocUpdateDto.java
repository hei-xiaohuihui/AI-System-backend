package com.lyh.aiSystem.pojo.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @author BigHH
 *  超级管理员更新知识文档数据传输对象
 */
@Data
public class KnowledgeDocUpdateDto {

    @NotNull
    private Long id;

    private String title;

    private String description;

    private String resourceUrl;
}
