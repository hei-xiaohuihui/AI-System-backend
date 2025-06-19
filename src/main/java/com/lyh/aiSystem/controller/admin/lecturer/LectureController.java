package com.lyh.aiSystem.controller.admin.lecturer;

import com.lyh.aiSystem.pojo.dto.LectureCreateDto;
import com.lyh.aiSystem.pojo.dto.LecturePageDtoForLecturer;
import com.lyh.aiSystem.pojo.dto.LectureRecreateDto;
import com.lyh.aiSystem.pojo.dto.LectureUpdateDto;
import com.lyh.aiSystem.service.LectureService;
import com.lyh.aiSystem.utils.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author BigHH
 *  管理员端——讲师讲座控制器
 */
@RequiredArgsConstructor
@RestController("lectureControllerForLecturer")
@RequestMapping("/admin/lecturer/lectures")
public class LectureController {

    private final LectureService  lectureService;

    /**
     *  讲师创建讲座
     * @param dto
     * @param file
     * @return
     */
    @PostMapping("/create")
    public Result createLecture(@RequestPart @Valid LectureCreateDto dto, @RequestPart("file") MultipartFile file) {
        lectureService.createLecture(dto, file);
        return Result.success();
    }

    /**
     *  讲师更新讲座信息
     * @param dto
     * @return
     */
    @PutMapping("/update")
    public Result updateLecture(@RequestBody @Valid LectureUpdateDto dto) {
        lectureService.updateLecture(dto);
        return Result.success();
    }

    /**
     *  讲师分页查询自己创建的讲座
     * @param dto
     * @return
     */
    @GetMapping("/page")
    public Result pageLectures(LecturePageDtoForLecturer dto) {
        return Result.success(lectureService.lecturePageForLecturer(dto));
    }

    /**
     *  删除讲座
     * @param id
     * @return
     */
    @DeleteMapping("/delete")
    public Result deleteLecture(@RequestParam("id") Long id, @RequestParam("resourceUrl") String resourceUrl) {
        lectureService.deleteLectureById(id, resourceUrl);
        return Result.success();
    }

    /**
     *  审核被拒绝的讲座可以修改后重新提交审核
     * @param dto
     * @return
     */
    @PostMapping("/recreate")
    public Result recreateLecture(@RequestPart @Valid LectureRecreateDto dto, @RequestPart(value = "file", required = false) MultipartFile file) {
        lectureService.recreateLecture(dto, file);
        return Result.success();
    }
}



