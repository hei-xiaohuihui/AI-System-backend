package com.lyh.aiSystem.controller.admin.superAdmin;

import com.lyh.aiSystem.pojo.dto.AdminCreateDto;
import com.lyh.aiSystem.pojo.dto.AdminPageDto;
import com.lyh.aiSystem.pojo.dto.AdminUpdateDto;
import com.lyh.aiSystem.pojo.dto.UserPageDto;
import com.lyh.aiSystem.service.AdminService;
import com.lyh.aiSystem.utils.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

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
     *  超级管理员更新普通管理员（讲师）信息接口
     * @param adminUpdateDto
     * @return
     */
    @PostMapping("/updateAdminInfo")
    public Result updateAdminInfo(@RequestBody @Valid AdminUpdateDto adminUpdateDto) {
        adminService.updateOtherAdminInfo(adminUpdateDto);
        return Result.success();
    }

    /**
     *  超级管理员分页查询讲师信息接口
     * @param adminPageDto
     * @return
     */
    @GetMapping("/adminPage")
    public Result adminPage(AdminPageDto adminPageDto) {
        return Result.success(adminService.adminPage(adminPageDto));
    }

    /**
     *  超级管理员更新讲师账户状态
     * @param adminId
     * @param status
     * @return
     */
    @PostMapping("/updateAdminStatus")
    public Result updateAdminStatus(@RequestParam("adminId") Long adminId, @RequestParam("status") Byte status) {
        adminService.updateAdminStatus(adminId, status);
        return Result.success();
    }

    /**
     *  超级管理员分页查询学生信息接口
     * @param userPageDto
     * @return
     */
    @GetMapping("/userPage")
    public Result userPage(UserPageDto userPageDto) {
        return Result.success(adminService.userPage(userPageDto));
    }

    /**
     *  超级管理员更新学生账户状态
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
