package com.lyh.aiSystem.controller.admin.lecturer;

import com.lyh.aiSystem.pojo.dto.LectureCreateDto;
import com.lyh.aiSystem.service.LectureService;
import com.lyh.aiSystem.utils.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author BigHH
 *  管理员端——讲师讲座控制器
 */
@RequiredArgsConstructor
@RestController("lectureControllerForLecturer")
@RequestMapping("/admin/lecturer/lectures")
public class LectureController {

    private final LectureService  lectureService;


    @PostMapping("/create")
    public Result createLecture(@RequestBody @Valid LectureCreateDto dto) {
        lectureService.createLecture(dto);
        return Result.success();
    }
}



