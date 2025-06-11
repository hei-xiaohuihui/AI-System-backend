package com.lyh.aiSystem.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author BigHH
 *  讲座实体类
 */
@Data
@TableName("cs_lecture")
public class Lecture {

    private Long id;

    private Long creatorId;

    // 讲座标题
    private String title;

    private String description;

    private String speakerName;

    // 讲师头衔
    private String speakerTitle;

    private LocalDateTime lectureTime;

    private String location;

    // 讲座标签
    private String tags;

    // 讲座资源url
    private String resourceUrl;

    // 对应的向量数据库中文档id
    private String ragDocId;

    // 讲座审核状态
    private String status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
