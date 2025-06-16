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
import com.lyh.aiSystem.service.LectureService;
import com.lyh.aiSystem.utils.UserContextUtil;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author BigHH
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class LectureEnrollServiceImpl implements LectureEnrollService {

    private final LectureMapper lectureMapper;

    private final LectureEnrollMapper lectureEnrollMapper;

    private final UserContextUtil userContextUtil;

    @Setter
    private Long userId; // AI调用tool时无法通过用户上下文工具获取用户信息

    /**
     *  用户（学生）报名讲座
     * @param lectureId
     */
    @Override
    public String enrollLecture(Long lectureId) {
        // 检查讲座是否存在
        Lecture lecture = lectureMapper.selectById(lectureId);
        if(lecture == null) {
            throw new BaseException(ExceptionEnum.LECTURE_NOT_EXIST); // 讲座不存在
        }
        log.error("报名讲座的用户id: {}", userId);
        // 检查用户是否已报名
        LectureEnroll lectureEnroll = lectureEnrollMapper.selectOne(
                new QueryWrapper<LectureEnroll>()
                        .eq("lecture_id", lectureId)
                        .eq("user_id", userId));
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
        lectureEnroll.setUserId(userId);
        lectureEnroll.setLectureId(lectureId);
        int insertResult = lectureEnrollMapper.insert(lectureEnroll);
        if(insertResult == 0) {
            throw new BaseException(ExceptionEnum.DB_INSERT_ERROR);
        }
        return "报名成功";
    }

    /**
     *  用户取消报名
     * @param lectureId
     */
    @Override
    public String cancelEnroll(Long lectureId) {
        // 检查讲座是否存在
        if(lectureMapper.selectById(lectureId) == null) {
            throw new BaseException(ExceptionEnum.LECTURE_NOT_EXIST); // 讲座不存在
        }
        // 检查用户是否已报名
        LectureEnroll lectureEnroll = lectureEnrollMapper.selectOne(
                new QueryWrapper<LectureEnroll>()
                        .eq("lecture_id", lectureId)
                        .eq("user_id", userId));
        if(lectureEnroll == null) {
            throw new BaseException(ExceptionEnum.LECTURE_USER_NOT_ENROLLED); // 用户未报名
        }
        // 删除报名记录
        int deleteResult = lectureEnrollMapper.delete(new QueryWrapper<LectureEnroll>().eq("user_id", userId).eq("lecture_id", lectureId));
        if(deleteResult == 0) {
            throw new BaseException(ExceptionEnum.DB_DELETE_ERROR);
        }
        return "取消报名成功";
    }

    /**
     *  分页查询用户已报名的所有讲座
     * @return
     */
    @Override
    public IPage<LecturePageVoForUserEnroll> getEnrollLectures(LecturePageDtoForUser dto) {
        // 根据用户id查询其报名的所有讲座
        List<LectureEnroll> lectureEnrolls = lectureEnrollMapper.selectList(new QueryWrapper<LectureEnroll>().eq("user_id", userId));
        if(CollectionUtils.isEmpty(lectureEnrolls)) {
            return new Page<>();
        }
        // 创建分页对象
        Page<Lecture> page = new Page<>(dto.getPageNum(), dto.getPageSize());
        QueryWrapper<Lecture> queryWrapper = new QueryWrapper<>();
        // 创建查询条件: 根据所有讲座id查询
        queryWrapper.in("id", lectureEnrolls.stream().map(lectureEnroll -> lectureEnroll.getLectureId()).collect(Collectors.toList())); // 注意不先判断lectureEnrolls为非空的话会导致SQL拼接错误// 模糊查询
        setCommonQueryWrapper(queryWrapper, dto.getTitle(), dto.getSpeakerName(), dto.getSpeakerTitle(), dto.getLocation(), dto.getTags(), dto.getStartTime(), dto.getEndTime());
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
     *  获取用户报名的所有讲座信息
     * @return
     */
    @Override
    public List<LecturePageVoForUserEnroll> getEnrollLectures() {
        List<LectureEnroll> lectureEnrolls = lectureEnrollMapper.selectList(new QueryWrapper<LectureEnroll>().eq("user_id", userId));
        if(CollectionUtils.isEmpty(lectureEnrolls)) {
            return List.of();
        }
        return lectureEnrolls.stream().map(lectureEnroll -> {
            LecturePageVoForUserEnroll vo = new LecturePageVoForUserEnroll();
            Lecture lecture = lectureMapper.selectById(lectureEnroll.getLectureId());
            BeanUtils.copyProperties(lecture, vo);
            vo.setEnrollCount(this.getEnrollCount(lectureEnroll.getLectureId()));
            vo.setEnrollTime(lectureEnroll.getEnrollTime());
            return vo;
        }).collect(Collectors.toList());
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

    /**
     * 设置查询条件
     */
    private void setCommonQueryWrapper(QueryWrapper<Lecture> queryWrapper, String title, String speakerName, String speakerTitle, String location, String tags, LocalDateTime startTime, LocalDateTime endTime) {
        if(StringUtils.hasText(title)) {
            queryWrapper.like("title", title);
        }
        if(StringUtils.hasText(speakerName)) {
            queryWrapper.like("speaker_name", speakerName);
        }
        if(StringUtils.hasText(speakerTitle)) {
            queryWrapper.eq("speaker_title", speakerTitle);
        }
        if(StringUtils.hasText(location)) {
            queryWrapper.like("location", location);
        }
        if(StringUtils.hasText(tags)) { // todo 实现tage不区分大小写
            queryWrapper.like("tags", tags);
        }
        if(startTime != null && endTime != null) {
            queryWrapper.between("lecture_time", startTime, endTime);
        }
    }

}