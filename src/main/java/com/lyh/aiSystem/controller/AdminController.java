package com.lyh.aiSystem.controller;

import com.lyh.aiSystem.pojo.dto.AdminCreateDto;
import com.lyh.aiSystem.pojo.dto.AdminLoginDto;
import com.lyh.aiSystem.pojo.dto.AdminUpdateDto;
import com.lyh.aiSystem.service.AdminService;
import com.lyh.aiSystem.utils.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

/**
 * @author BigHH
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/auth")
public class AdminController {

    private final AdminService adminService;

    /**
     *  管理员登录
     * @param adminLoginDto
     * @return
     */
    @PostMapping("/login")
    public Result adminLogin(@RequestBody @Valid AdminLoginDto adminLoginDto) {
        return Result.success(adminService.adminLogin(adminLoginDto));
    }

    /**
     *  管理员更新信息
     * @param adminUpdateDto
     * @return
     */
    @PostMapping("/update")
    public Result updateAdminInfo(@RequestBody @Valid AdminUpdateDto adminUpdateDto) {
        adminService.updateAdminInfo(adminUpdateDto);
        return Result.success();
    }
}
