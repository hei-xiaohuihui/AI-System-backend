package com.lyh.aiSystem.pojo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * @author BigHH
 *  讲师再次提交讲座审核数据传输对象
 */
@Data
public class LectureRecreateDto {

    @NotNull
    private Long id;

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

    private String resourceUrl;

//    private String ragDocId;

    private Integer capacity;
}
