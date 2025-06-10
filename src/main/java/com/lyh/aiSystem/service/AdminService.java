package com.lyh.aiSystem.service;

import com.lyh.aiSystem.pojo.dto.AdminCreateDto;
import com.lyh.aiSystem.pojo.dto.AdminLoginDto;
import com.lyh.aiSystem.pojo.dto.AdminUpdateDto;

public interface AdminService {

    String adminLogin(AdminLoginDto adminLoginDto);

    void updateAdminInfo(AdminUpdateDto adminUpdateDto);

    void createAdmin(AdminCreateDto adminCreateDto);
}
