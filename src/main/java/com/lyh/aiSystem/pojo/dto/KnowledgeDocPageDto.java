package com.lyh.aiSystem.pojo.dto;

import lombok.Data;

/**
 * @author BigHH
 *  知识文档分页查询数据传输对象
 */
@Data
public class KnowledgeDocPageDto {

    private int pageNum = 1;

    private int pageSize = 10;

    private String title;

    private String description;
}
