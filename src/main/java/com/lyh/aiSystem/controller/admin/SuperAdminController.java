package com.lyh.aiSystem.controller.admin;

import com.lyh.aiSystem.pojo.dto.AdminCreateDto;
import com.lyh.aiSystem.pojo.dto.AdminUpdateDto;
import com.lyh.aiSystem.service.AdminService;
import com.lyh.aiSystem.utils.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author BigHH
 *  超级管理员控制器
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/superAdmin")
public class SuperAdminController {

    private final AdminService adminService;

    /**
     *  超级管理员创建普通管理员（讲师）接口
     * @param adminCreateDto
     * @return
     */
    @PostMapping("/createAdmin")
    public Result createAdmin(@RequestBody @Valid AdminCreateDto adminCreateDto) {
        adminService.createAdmin(adminCreateDto);
        return Result.success();
    }
}
