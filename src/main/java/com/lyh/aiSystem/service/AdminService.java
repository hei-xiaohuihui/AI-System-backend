package com.lyh.aiSystem.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lyh.aiSystem.pojo.dto.AdminCreateDto;
import com.lyh.aiSystem.pojo.dto.AdminLoginDto;
import com.lyh.aiSystem.pojo.dto.AdminUpdateDto;
import com.lyh.aiSystem.pojo.dto.UserPageDto;
import com.lyh.aiSystem.pojo.vo.UserPageVo;

public interface AdminService {

    String adminLogin(AdminLoginDto adminLoginDto);

    void updateAdminInfo(AdminUpdateDto adminUpdateDto);

    void createAdmin(AdminCreateDto adminCreateDto);

    void updateUserStatus(Long userId, Byte status);

    IPage<UserPageVo> userPage(UserPageDto userPageDto);
}
