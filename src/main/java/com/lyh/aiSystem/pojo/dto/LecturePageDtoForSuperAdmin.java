package com.lyh.aiSystem.pojo.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * @author BigHH
 *  讲座分页查询数据传输对象——超级管理员
 */
@Data
public class LecturePageDtoForSuperAdmin {

    private int pageNum = 1;

    private int pageSize = 10;

    // 支持对以下字段的模糊查询
    private String title;

    private String speakerName;

    private String speakerTitle;

    private String location;

    private String tags;

    private String status;

    // 时间范围查询
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime  startTime;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime  endTime;

}
