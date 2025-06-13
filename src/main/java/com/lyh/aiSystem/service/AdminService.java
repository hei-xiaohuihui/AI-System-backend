package com.lyh.aiSystem.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lyh.aiSystem.pojo.dto.*;
import com.lyh.aiSystem.pojo.vo.AdminPageVo;
import com.lyh.aiSystem.pojo.vo.AdminVo;
import com.lyh.aiSystem.pojo.vo.UserPageVo;
import org.springframework.web.multipart.MultipartFile;

public interface AdminService {

    String adminLogin(AdminLoginDto adminLoginDto);

    void updateAdminInfo(AdminUpdateDto adminUpdateDto);

    void createAdmin(AdminCreateDto adminCreateDto);

    void updateUserStatus(Long userId, Byte status);

    IPage<UserPageVo> userPage(UserPageDto userPageDto);

    IPage<AdminPageVo> adminPage(AdminPageDto adminPageDto);

    void updateAdminStatus(Long adminId, Byte status);

    void updateOtherAdminInfo(AdminUpdateDto adminUpdateDto);

    AdminVo getAdminDetail();

    String uploadFile(MultipartFile file);
}
