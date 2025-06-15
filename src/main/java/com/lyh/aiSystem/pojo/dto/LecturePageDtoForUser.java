package com.lyh.aiSystem.pojo.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * @author BigHH
 *  讲座分页查询数据传输对象——用户端（学生）
 */
@Data
public class LecturePageDtoForUser {

    private int pageNum = 1;

    private int pageSize = 10;

    // 模糊查询
    private String title;

    private String speakerName;

    private String speakerTitle;

    private String location;

    private String tags;

    // 时间范围查询
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime  endTime;
}
