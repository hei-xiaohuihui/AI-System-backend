package com.lyh.aiSystem.controller.user;

import com.lyh.aiSystem.pojo.dto.LecturePageDtoForUser;
import com.lyh.aiSystem.service.LectureEnrollService;
import com.lyh.aiSystem.service.LectureService;
import com.lyh.aiSystem.utils.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * @author BigHH
 *  普通用户（学生）讲座控制器
 */
@RequiredArgsConstructor
@RestController("lectureControllerForUser")
@RequestMapping("/user/lectures")
public class LectureController {

    private final LectureService lectureService;

    private final LectureEnrollService lectureEnrollService;

    /**
     *  用户分页查询讲座信息
     * @param dto
     * @return
     */
    @GetMapping("/page")
    public Result pageLectures(LecturePageDtoForUser dto) {
        return Result.success(lectureService.lecturePageForUser(dto));
    }

    /**
     *  用户报名讲座
     * @param lectureId
     * @return
     */
    @PostMapping("/enroll")
    public Result enrollLecture(@RequestParam("lectureId") Long lectureId) {
        lectureEnrollService.enrollLecture(lectureId);
        return Result.success();
    }

    /**
     *  用户取消报名
     * @param lectureId
     * @return
     */
    @DeleteMapping("/cancel")
    public Result cancelEnroll(@RequestParam("lectureId") Long lectureId) {
        lectureEnrollService.cancelEnroll(lectureId);
        return Result.success();
    }

    /**
     *  获取用户报名的所有讲座信息
     * @return
     */
    @GetMapping("/getEnroll")
    public Result getEnrollLectures(LecturePageDtoForUser dto) {
        return Result.success(lectureEnrollService.getEnrollLectures(dto));
    }
}
