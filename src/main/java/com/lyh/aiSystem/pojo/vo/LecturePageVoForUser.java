package com.lyh.aiSystem.pojo.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author BigHH
 *  学生讲座视图对象
 */
@Data
public class LecturePageVoForUser {

    private Long id;

    private String title;

    private String speakerName;

    private String speakerTitle;

    private String description;

    private LocalDateTime lectureTime;

    private String location;

    private String tags;

    private Integer capacity;

    private String resourceUrl;

    // 报名人数：需要通过计算得出
    private Integer enrollCount;
}
