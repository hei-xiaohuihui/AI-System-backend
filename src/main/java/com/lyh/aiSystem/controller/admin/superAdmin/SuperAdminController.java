package com.lyh.aiSystem.controller.admin.superAdmin;

import com.lyh.aiSystem.pojo.dto.AdminCreateDto;
import com.lyh.aiSystem.pojo.dto.UserPageDto;
import com.lyh.aiSystem.service.AdminService;
import com.lyh.aiSystem.utils.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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

    /**
     *  超级管理员分页查询普通用户信息接口
     * @param userPageDto
     * @return
     */
    @PostMapping("/userPage")
    public Result userPage(UserPageDto userPageDto) {
        return Result.success(adminService.userPage(userPageDto));
    }

    /**
     *  超级管理员更新普通管理员状态接口
     * @param userId
     * @param status
     * @return
     */
    @PostMapping("/updateUserStatus")
    public Result updateUserStatus(@RequestParam("userId") Long userId, @RequestParam("status") Byte status) {
        adminService.updateUserStatus(userId, status);
        return Result.success();
    }

}
