package com.lyh.aiSystem.pojo.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author BigHH
 *  讲座分页查询视图对象——超级管理员端
 */
@Data
public class LecturePageVoForSuperAdmin {

    private Long id;

    private String title;

    private String description;

    private String speakerName;

    private String speakerTitle;

    private String location;

    private LocalDateTime lectureTime;

    private String tags;

    private Integer capacity;

    // 报名人数：需要通过计算得出
    private Integer enrollCount;

    private String resourceUrl;

    private String ragDocId;

    private String status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
