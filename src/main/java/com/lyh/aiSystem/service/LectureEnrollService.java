package com.lyh.aiSystem.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lyh.aiSystem.pojo.dto.LecturePageDtoForUser;
import com.lyh.aiSystem.pojo.vo.LecturePageVoForUserEnroll;

public interface LectureEnrollService {

    void enrollLecture(Long lectureId);

    Integer getEnrollCount(Long lectureId);

    void cancelEnroll(Long lectureId);

    IPage<LecturePageVoForUserEnroll> getEnrollLectures(LecturePageDtoForUser dto);
}
