package com.lyh.aiSystem.pojo.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * @author BigHH
 *  讲师创建讲座数据传输对象
 */
@Data
public class LectureCreateDto {

    @NotBlank
    private String title;

    @NotBlank
    private String description;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lectureTime;

    @NotBlank
    private String location;

    private String tags;

//    @NotBlank
//    private String resourceUrl;
//
//    private String ragDocId;

    private Integer capacity;
}
