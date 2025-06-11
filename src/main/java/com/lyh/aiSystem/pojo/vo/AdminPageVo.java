package com.lyh.aiSystem.pojo.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author BigHH
 */
@Data
public class AdminPageVo {

    private Long id;

    private String username;

    private String lecturerName;

    private String lecturerTitle;

    private String email;

    private String phone;

    private Byte gender;

    private Byte status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
