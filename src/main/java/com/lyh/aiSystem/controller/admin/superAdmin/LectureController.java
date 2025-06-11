package com.lyh.aiSystem.controller.admin.superAdmin;

import com.lyh.aiSystem.pojo.dto.LecturePageDtoForSuperAdmin;
import com.lyh.aiSystem.service.LectureService;
import com.lyh.aiSystem.utils.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * @author BigHH
 *  管理员端——超级管理员讲座控制器
 */
@RequiredArgsConstructor
@RestController("lectureControllerForSuperAdmin")
@RequestMapping("/admin/superAdmin/lectures")
public class LectureController {

    private final LectureService lectureService;

    /**
     *  超级管理员审核讲座：修改讲座状态
     * @param id
     * @param status
     * @return
     */
    @PutMapping("/check")
    public Result checkLecture(@RequestParam("id") Long id, @RequestParam("status") String status) {
        lectureService.checkLecture(id, status);
        return Result.success();
    }

    /**
     *  超级管理员获取所有讲座列表
     * @param dto
     * @return
     */
    @GetMapping("/page")
    public Result pageLectures(LecturePageDtoForSuperAdmin dto) {
        return Result.success(lectureService.lecturePageForSuperAdmin(dto));
    }
}
