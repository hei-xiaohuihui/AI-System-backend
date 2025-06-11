package com.lyh.aiSystem.pojo.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * @author BigHH
 *  讲座分页查询数据传输对象——讲师
 */
@Data
public class LecturePageDtoForLecturer {

    private int pageNum = 1;

    private int pageSize = 10;

    // 支持对title、location、tags的模糊查询
    private String title;

    private String location;

    private String tags;

    // 支持对status的精确查询
    private String status;

    // 根据时间范围查询
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;
}
