package com.lyh.aiSystem.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author BigHH
 *  讲座报名表
 */
@Data
@TableName("cs_lecture_enroll")
public class LectureEnroll {

    private Long id;

    private Long userId;

    private Long lectureId;

    private LocalDateTime enrollTime;
}
