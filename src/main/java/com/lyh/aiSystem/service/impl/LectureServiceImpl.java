package com.lyh.aiSystem.service.impl;

import com.lyh.aiSystem.mapper.LectureMapper;
import com.lyh.aiSystem.pojo.dto.LectureCreateDto;
import com.lyh.aiSystem.service.LectureService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author BigHH
 */
@RequiredArgsConstructor
@Service
public class LectureServiceImpl implements LectureService {

    private final LectureMapper  lectureMapper;

    /**
     *  讲师创建讲座
     * @param dto
     */
    @Override
    public void createLecture(LectureCreateDto dto) {
        //
    }
}
