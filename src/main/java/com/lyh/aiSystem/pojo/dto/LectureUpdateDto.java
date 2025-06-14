package com.lyh.aiSystem.pojo.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * @author BigHH
 *  讲师更新讲座信息数据传输对象
 */
@Data
public class LectureUpdateDto {

    @NotNull
    private Long id;

    private String title;

    private String description;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lectureTime;

    private String location;

    private String tags;

//    private String resourceUrl;
//
////    private String ragDocId;

    private Integer capacity;
}
