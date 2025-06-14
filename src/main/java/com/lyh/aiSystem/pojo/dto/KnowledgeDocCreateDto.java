package com.lyh.aiSystem.pojo.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @author BigHH
 *  超级管理创建知识文档数据传输对象
 */
@Data
public class KnowledgeDocCreateDto {

    @NotBlank
    private String title;

    @NotBlank
    private String description;

}
