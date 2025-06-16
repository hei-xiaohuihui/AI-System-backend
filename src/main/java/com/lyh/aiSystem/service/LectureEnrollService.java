package com.lyh.aiSystem.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lyh.aiSystem.pojo.dto.LecturePageDtoForUser;
import com.lyh.aiSystem.pojo.vo.LecturePageVoForUserEnroll;

import java.util.List;

public interface LectureEnrollService {

    String enrollLecture(Long lectureId);

    Integer getEnrollCount(Long lectureId);

    String cancelEnroll(Long lectureId);

    IPage<LecturePageVoForUserEnroll> getEnrollLectures(LecturePageDtoForUser dto);

    List<LecturePageVoForUserEnroll> getEnrollLectures();
}
