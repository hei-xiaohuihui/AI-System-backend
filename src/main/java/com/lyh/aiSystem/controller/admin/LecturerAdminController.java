package com.lyh.aiSystem.controller.admin;

import com.lyh.aiSystem.service.AdminService;
import lombok.RequiredArgsConstructor;
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


}
