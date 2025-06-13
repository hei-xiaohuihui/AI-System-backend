package com.lyh.aiSystem.pojo.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author BigHH
 *  讲师讲座视图对象
 */
@Data
public class LecturePageVoForLecturer {

    private Long id;

    private String title;

    private String description;

    private LocalDateTime lectureTime;

    private String location;

    private String tags;

    private Integer capacity;

    // 报名人数：需要通过计算得出
    private Integer enrollCount;

    private String resourceUrl;

//    private String ragDocId;

    private String status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
