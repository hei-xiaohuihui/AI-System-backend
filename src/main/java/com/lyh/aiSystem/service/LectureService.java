package com.lyh.aiSystem.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lyh.aiSystem.pojo.dto.*;
import com.lyh.aiSystem.pojo.vo.LecturePageVoForLecturer;
import com.lyh.aiSystem.pojo.vo.LecturePageVoForSuperAdmin;
import com.lyh.aiSystem.pojo.vo.LecturePageVoForUser;

public interface LectureService {

    void createLecture(LectureCreateDto dto);

    void updateLecture(LectureUpdateDto dto);

    IPage<LecturePageVoForLecturer> lecturePageForLecturer(LecturePageDtoForLecturer dto);

    void deleteLectureById(Long id, String resourceUrl);

    void checkLecture(Long id, String status);

    IPage<LecturePageVoForSuperAdmin> lecturePageForSuperAdmin(LecturePageDtoForSuperAdmin dto);

    IPage<LecturePageVoForUser> lecturePageForUser(LecturePageDtoForUser dto);

    void recreateLecture(LectureRecreateDto dto);
}
