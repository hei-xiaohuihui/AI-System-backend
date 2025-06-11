package com.lyh.aiSystem.pojo.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * @author BigHH
 *  讲师创建讲座数据传输对象
 */
@Data
public class LectureCreateDto {

    @NotNull
    private String title;

    @NotNull
    private String description;

    @NotNull
    private LocalDateTime lectureTime;

    @NotNull
    private String location;


    private String tags;

    private String resourceUrl;

    private String ragDocId;
}
