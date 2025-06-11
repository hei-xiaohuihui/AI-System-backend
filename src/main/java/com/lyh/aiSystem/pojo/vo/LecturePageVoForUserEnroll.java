package com.lyh.aiSystem.pojo.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author BigHH
 *  用户已报名讲座信息视图对象
 */
@Data
public class LecturePageVoForUserEnroll {

    private Long id;

    private String title;

    private String description;

    private LocalDateTime lectureTime;

    private String location;

    private String tags;

    private Integer capacity;

    // 报名人数：需要通过计算得出
    private Integer enrollCount;

    // 报名时间
    private LocalDateTime enrollTime;
}
