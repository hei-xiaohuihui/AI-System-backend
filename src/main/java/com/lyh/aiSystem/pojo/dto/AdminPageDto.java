package com.lyh.aiSystem.pojo.dto;

import lombok.Data;

/**
 * @author BigHH
 */
@Data
public class AdminPageDto {

    private int pageNum = 1;

    private int pageSize = 10;

    // 支持对lecturerName、lecturerTitle、email、phone进行模糊查询
    private String lecturerName;

    private String lecturerTitle;

    private String email;

    private String phone;

    // 支持对status的条件查询
    private Byte status;
}
