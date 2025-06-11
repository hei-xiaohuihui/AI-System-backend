package com.lyh.aiSystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lyh.aiSystem.enumeration.ExceptionEnum;
import com.lyh.aiSystem.exception.BaseException;
import com.lyh.aiSystem.mapper.LectureEnrollMapper;
import com.lyh.aiSystem.mapper.LectureMapper;
import com.lyh.aiSystem.pojo.dto.LecturePageDtoForUser;
import com.lyh.aiSystem.pojo.entity.Lecture;
import com.lyh.aiSystem.pojo.entity.LectureEnroll;
import com.lyh.aiSystem.pojo.vo.LecturePageVoForUserEnroll;
import com.lyh.aiSystem.service.LectureEnrollService;
import com.lyh.aiSystem.utils.UserContextUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author BigHH
 */
@RequiredArgsConstructor
@Service
public class LectureEnrollServiceImpl implements LectureEnrollService {

    private final LectureMapper lectureMapper;

    private final LectureEnrollMapper lectureEnrollMapper;

    private final UserContextUtil userContextUtil;

    /**
     *  用户（学生）报名讲座
     * @param lectureId
     */
    @Override
    public void enrollLecture(Long lectureId) {
        // 检查讲座是否存在
        Lecture lecture = lectureMapper.selectById(lectureId);
        if(lecture == null) {
            throw new BaseException(ExceptionEnum.LECTURE_NOT_EXIST); // 讲座不存在
        }
        // 检查用户是否已报名
        LectureEnroll lectureEnroll = lectureEnrollMapper.selectOne(
                new QueryWrapper<LectureEnroll>()
                        .eq("lecture_id", lectureId)
                        .eq("user_id", userContextUtil.getUserId()));
        if(lectureEnroll != null) {
            throw new BaseException(ExceptionEnum.LECTURE_USER_ALREADY_ENROLLED); // 用户已报名
        }
        // 检查讲座是否已选满
        Integer enrollCount = this.getEnrollCount(lectureId); // 获取已报名人数
        if(enrollCount >= lecture.getCapacity()) {
            throw new  BaseException(ExceptionEnum.LECTURE_ALREADY_FULL); // 讲座已选满
        }
        // 插入报名记录
        lectureEnroll = new LectureEnroll();
        lectureEnroll.setUserId(userContextUtil.getUserId());
        lectureEnroll.setLectureId(lectureId);
        int insertResult = lectureEnrollMapper.insert(lectureEnroll);
        if(insertResult == 0) {
            throw new BaseException(ExceptionEnum.DB_INSERT_ERROR);
        }
    }

    /**
     *  用户取消报名
     * @param lectureId
     */
    @Override
    public void cancelEnroll(Long lectureId) {
        // 检查讲座是否存在
        if(lectureMapper.selectById(lectureId) == null) {
            throw new BaseException(ExceptionEnum.LECTURE_NOT_EXIST); // 讲座不存在
        }
        // 检查用户是否已报名
        LectureEnroll lectureEnroll = lectureEnrollMapper.selectOne(
                new QueryWrapper<LectureEnroll>()
                        .eq("lecture_id", lectureId)
                        .eq("user_id", userContextUtil.getUserId()));
        if(lectureEnroll == null) {
            throw new BaseException(ExceptionEnum.LECTURE_USER_NOT_ENROLLED); // 用户未报名
        }
        // 删除报名记录
        int deleteResult = lectureEnrollMapper.delete(new QueryWrapper<LectureEnroll>().eq("user_id", userContextUtil.getUserId()).eq("lecture_id", lectureId));
        if(deleteResult == 0) {
            throw new BaseException(ExceptionEnum.DB_DELETE_ERROR);
        }
    }

    /**
     *  分页查询用户已报名的所有讲座
     * @return
     */
    @Override
    public IPage<LecturePageVoForUserEnroll> getEnrollLectures(LecturePageDtoForUser dto) {
        // 根据用户id查询其报名的所有讲座
        List<LectureEnroll> lectureEnrolls = lectureEnrollMapper.selectList(new QueryWrapper<LectureEnroll>().eq("user_id", userContextUtil.getUserId()));
        // 创建分页对象
        Page<Lecture> page = new Page<>(dto.getPageNum(), dto.getPageSize());
        QueryWrapper<Lecture> queryWrapper = new QueryWrapper<>();
        // 创建查询条件: 根据所有讲座id查询
        queryWrapper.in("id", lectureEnrolls.stream().map(lectureEnroll -> lectureEnroll.getLectureId()).collect(Collectors.toList()));
        queryWrapper.orderByAsc("lecture_time");
        IPage<Lecture> lectures = lectureMapper.selectPage(page, queryWrapper);
        // 封装为Vo对象
        List<LecturePageVoForUserEnroll> voList = lectures.getRecords().stream().map(lecture -> {
            LecturePageVoForUserEnroll vo = new LecturePageVoForUserEnroll();
            BeanUtils.copyProperties(lecture, vo);
            // 计算报名人数
            vo.setEnrollCount(this.getEnrollCount(lecture.getId()));
            // 设置报名时间
            for(LectureEnroll lectureEnroll : lectureEnrolls) {
                if(lectureEnroll.getLectureId().equals(lecture.getId())) {
                    vo.setEnrollTime(lectureEnroll.getEnrollTime());
                }
            }
            return vo;
        }).collect(Collectors.toList());
        // 封装为PageVo对象
        Page<LecturePageVoForUserEnroll> pageVo = new Page<>();
        pageVo.setCurrent(lectures.getCurrent());
        pageVo.setSize(lectures.getSize());
        pageVo.setTotal(lectures.getTotal());
        pageVo.setRecords(voList);
        return pageVo;
    }

    /**
     *  获取讲座报名人数
     * @param lectureId
     * @return
     */
    @Override
    public Integer getEnrollCount(Long lectureId) {
        Long count = lectureEnrollMapper.selectCount(
                new QueryWrapper<LectureEnroll>()
                        .eq("lecture_id", lectureId));
        return count != null ? count.intValue() : 0;
    }
}