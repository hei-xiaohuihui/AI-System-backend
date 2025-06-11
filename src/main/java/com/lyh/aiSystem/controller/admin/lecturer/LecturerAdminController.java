package com.lyh.aiSystem.controller.admin.lecturer;

import com.lyh.aiSystem.service.AdminService;
import com.lyh.aiSystem.utils.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author BigHH
 *  管理员——讲师控制器
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/lecturer")
public class LecturerAdminController {

    private final AdminService adminService;

    @GetMapping("/test")
    public Result test() {
        return Result.success();
    }
}
