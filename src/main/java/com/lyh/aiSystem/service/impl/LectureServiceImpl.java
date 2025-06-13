package com.lyh.aiSystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lyh.aiSystem.constant.AdminRoleConstant;
import com.lyh.aiSystem.constant.LectureStatusConstant;
import com.lyh.aiSystem.enumeration.ExceptionEnum;
import com.lyh.aiSystem.exception.BaseException;
import com.lyh.aiSystem.mapper.AdminMapper;
import com.lyh.aiSystem.mapper.LectureMapper;
import com.lyh.aiSystem.pojo.dto.*;
import com.lyh.aiSystem.pojo.entity.Admin;
import com.lyh.aiSystem.pojo.entity.Lecture;
import com.lyh.aiSystem.pojo.vo.LecturePageVoForLecturer;
import com.lyh.aiSystem.pojo.vo.LecturePageVoForSuperAdmin;
import com.lyh.aiSystem.pojo.vo.LecturePageVoForUser;
import com.lyh.aiSystem.service.LectureEnrollService;
import com.lyh.aiSystem.service.LectureService;
import com.lyh.aiSystem.utils.AdminContextUtil;
import io.netty.util.internal.StringUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
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
public class LectureServiceImpl implements LectureService {

    private final LectureMapper  lectureMapper;

    private final AdminContextUtil adminContextUtil;

    private final AdminMapper adminMapper;

    private final LectureEnrollService  lectureEnrollService;

    /**
     *  讲师创建讲座
     * @param dto
     */
    @Override
    public void createLecture(LectureCreateDto dto) {
        Lecture lecture = new Lecture();
        // 设置属性
        // 获取当前登录讲师的主键id，根据id查询其所有信息
        Admin lecturer = adminMapper.selectOne(new QueryWrapper<Admin>().eq("id", adminContextUtil.getAdminId()));
        if(lecturer == null) {
            throw new BaseException(ExceptionEnum.ADMIN_NOT_EXIST);
        }
        lecture = Lecture.builder()
                .creatorId(lecturer.getId())
                .speakerName(lecturer.getLecturerName())
                .speakerTitle(lecturer.getLecturerTitle())
                .status(LectureStatusConstant.LECTURE_STATUS_PENDING) // 新建的讲座默认为待审核状态
                .build();

        // 拷贝属性
        BeanUtils.copyProperties(dto, lecture);

        // 插入数据库
        int insertResult = lectureMapper.insert(lecture);
        if (insertResult == 0) {
            throw new BaseException(ExceptionEnum.DB_INSERT_ERROR);
        }
    }

    /**
     *  讲师更新讲座信息
     * @param dto
     */
    @Override
    public void updateLecture(LectureUpdateDto dto) {
        // 判断dto中是否有字段需要更新
        if(!isDtoEffective(dto)) {
            return;
        }
        // 根据id查询该讲座信息
        Lecture lecture = lectureMapper.selectById(dto.getId());
        if(lecture == null) {
            throw new BaseException(ExceptionEnum.LECTURE_NOT_EXIST);
        }
        // 拷贝属性，并更新
        BeanUtils.copyProperties(dto, lecture);
        int updateResult = lectureMapper.updateById(lecture);
        if (updateResult == 0) {
            throw new BaseException(ExceptionEnum.DB_UPDATE_ERROR);
        }
    }

    /**
     *  讲师分页查询自己创建的讲座
     * @param dto
     * @return
     */
    @Override
    public IPage<LecturePageVoForLecturer> lecturePageForLecturer(LecturePageDtoForLecturer dto) {
        Page<Lecture> page = new Page<>(dto.getPageNum(), dto.getPageSize());
        QueryWrapper<Lecture> queryWrapper = new QueryWrapper<>();
        // 设置查询条件
        setCommonQueryWrapper(queryWrapper, dto.getTitle(), null, null, dto.getLocation(), dto.getTags(), dto.getStatus(), dto.getStartTime(), dto.getEndTime());
        // 设置及其他查询条件
        queryWrapper.eq("creator_id", adminContextUtil.getAdminId());
//        queryWrapper.orderByAsc("lecture_time"); // 根据讲座开始时间升序排列
        queryWrapper.orderByDesc("created_at"); // 根据创建时间降序排列
        IPage<Lecture> lectures = lectureMapper.selectPage(page, queryWrapper);
        // 封装为LecturePageVoForLecturer对象
        List<LecturePageVoForLecturer> voList = lectures.getRecords().stream().map(lecture -> {
            LecturePageVoForLecturer vo = new LecturePageVoForLecturer();
            BeanUtils.copyProperties(lecture, vo);
            // 计算讲座的报名人数
            vo.setEnrollCount(lectureEnrollService.getEnrollCount(lecture.getId()));
            return vo;
        }).collect(Collectors.toList());

        Page<LecturePageVoForLecturer> pageVo = new Page<>();
        pageVo.setCurrent(lectures.getCurrent());
        pageVo.setSize(lectures.getSize());
        pageVo.setTotal(lectures.getTotal());
        pageVo.setRecords(voList);
        return pageVo;
    }

    /**
     *  讲师根据id删除讲座
     * @param id
     */
    @Override
    public void deleteLectureById(Long id) {
        // 先判断讲座是否存在、是否是当前讲师创建的讲座
        Lecture lecture = lectureMapper.selectById(id);
        if(lecture == null) { // 讲座不存在
            throw new BaseException(ExceptionEnum.LECTURE_NOT_EXIST);
        }
        if(!lecture.getCreatorId().equals(adminContextUtil.getAdminId())) { // 讲座不属于当前讲师
            throw new BaseException(ExceptionEnum.LECTURE_NOT_BELONG_TO_CURRENT_ADMIN);
        }
        if(lecture.getStatus().equals(LectureStatusConstant.LECTURE_STATUS_APPROVED)) { // 讲座已通过审核，不能删除
            throw new BaseException(ExceptionEnum.LECTURE_STATUS_APPROVED_CANNOT_DELETE);
        }

        int deleteResult = lectureMapper.deleteById(id);
        if(deleteResult == 0) {
            throw new BaseException(ExceptionEnum.DB_DELETE_ERROR);
        }
    }

    /**
     *  超级管理员审核讲座
     * @param id
     * @param status
     */
    @Override
    public void checkLecture(Long id, String status) {
        Lecture lecture = lectureMapper.selectById(id);
        if(lecture == null) {
            throw new BaseException(ExceptionEnum.LECTURE_NOT_EXIST);
        }
        // 检查前端传入的status是否合法
        isLectureStatusEffective(status);
        lecture.setStatus(status);
        int updateResult = lectureMapper.updateById(lecture);
        if(updateResult == 0) {
            throw new BaseException(ExceptionEnum.DB_UPDATE_ERROR);
        }
    }

    /**
     *  超级管理员分页查询所有讲座信息
     * @param dto
     * @return
     */
    @Override
    public IPage<LecturePageVoForSuperAdmin> lecturePageForSuperAdmin(LecturePageDtoForSuperAdmin dto) {
        // 先判断是否是超级管理员
        if(!adminContextUtil.getAdminRole().equals(AdminRoleConstant.ADMIN_ROLE_SUPER_ADMIN)) {
            throw new BaseException(ExceptionEnum.ADMIN_NOT_SUPPER_ADMIN);
        }
        // 创建分页对象
        Page<Lecture> page = new Page<>(dto.getPageNum(), dto.getPageSize());
        // 创建查询条件
        QueryWrapper<Lecture> queryWrapper = new QueryWrapper<>();
        // 设置查询条件
        setCommonQueryWrapper(queryWrapper, dto.getTitle() , dto.getSpeakerName(), dto.getSpeakerTitle(), dto.getLocation(), dto.getTags(), dto.getStatus(), dto.getStartTime(), dto.getEndTime());
        queryWrapper.orderByDesc("created_at");
        IPage<Lecture> lectures = lectureMapper.selectPage(page, queryWrapper);

        // 封装为Vo对象
        List<LecturePageVoForSuperAdmin> voList = lectures.getRecords().stream().map(lecture -> {
            LecturePageVoForSuperAdmin vo = new LecturePageVoForSuperAdmin();
            BeanUtils.copyProperties(lecture, vo);
            // 计算讲座的报名人数
            vo.setEnrollCount(lectureEnrollService.getEnrollCount(lecture.getId()));
            return vo;
        }).collect(Collectors.toList());

        // 封装为PageVo对象
        Page<LecturePageVoForSuperAdmin> pageVo = new Page<>();
        pageVo.setCurrent(lectures.getCurrent());
        pageVo.setSize(lectures.getSize());
        pageVo.setTotal(lectures.getTotal());
        pageVo.setRecords(voList);
        return pageVo;
    }

    /**
     *  用户分页查询所有讲座信息
     * @param dto
     * @return
     */
    @Override
    public IPage<LecturePageVoForUser> lecturePageForUser(LecturePageDtoForUser dto) {
        Page<Lecture> page = new Page<>(dto.getPageNum(), dto.getPageSize());
        QueryWrapper<Lecture> queryWrapper = new QueryWrapper<>();
        // 设置查询条件
        setCommonQueryWrapper(queryWrapper, dto.getTitle(), dto.getSpeakerName(), dto.getSpeakerTitle(), dto.getLocation(), dto.getTags(), null, dto.getStartTime(), dto.getEndTime());
        queryWrapper.eq("status", LectureStatusConstant.LECTURE_STATUS_APPROVED); // 只能查询到审核通过的讲座
        queryWrapper.orderByAsc("lecture_time"); // 更具讲座开始时间升序排列
        IPage<Lecture> lectures = lectureMapper.selectPage(page, queryWrapper);
        List<LecturePageVoForUser> voList = lectures.getRecords().stream().map(lecture -> {
            LecturePageVoForUser vo = new LecturePageVoForUser();
            BeanUtils.copyProperties(lecture, vo);
            // 获取讲座的报名人数
            Integer enrollCount = lectureEnrollService.getEnrollCount(lecture.getId());
            vo.setEnrollCount(enrollCount);
            return vo;
        }).collect(Collectors.toList());

        // 封装为PageVo对象
        Page<LecturePageVoForUser> pageVo = new Page<>();
        pageVo.setCurrent(lectures.getCurrent());
        pageVo.setSize(lectures.getSize());
        pageVo.setTotal(lectures.getTotal());
        pageVo.setRecords(voList);
        return pageVo;
    }

    /**
     *  讲师重新创建讲座（将被拒绝的讲座修改后再次提交审核）
     * @param dto
     */
    @Override
    public void recreateLecture(LectureRecreateDto dto) {
        // 根据id查询被拒讲座信息
        Lecture lecture = lectureMapper.selectById(dto.getId());
        if(lecture == null) { // 讲座不存在
            throw new BaseException(ExceptionEnum.LECTURE_NOT_EXIST);
        }
        if(!lecture.getCreatorId().equals(adminContextUtil.getAdminId())) { // 讲座不属于当前讲师
            throw new BaseException(ExceptionEnum.LECTURE_NOT_BELONG_TO_CURRENT_ADMIN);
        }
        if(!lecture.getStatus().equals(LectureStatusConstant.LECTURE_STATUS_REJECTED)) { // 只有被拒的讲座才能被修改后重新提交
            throw new BaseException(ExceptionEnum.LECTURE_STATUS_INVALID);
        }
        // 属性拷贝
        BeanUtils.copyProperties(dto, lecture);
        // 将讲座状态改为待审核
        lecture.setStatus(LectureStatusConstant.LECTURE_STATUS_PENDING);
        int updateResult = lectureMapper.updateById(lecture);
        if(updateResult == 0) {
            throw new BaseException(ExceptionEnum.DB_UPDATE_ERROR);
        }
    }

    /**
     * 设置查询条件辅助方法
     */
    private void setCommonQueryWrapper(QueryWrapper<Lecture> queryWrapper, String title, String speakerName, String speakerTitle, String location, String tags, String status, LocalDateTime startTime, LocalDateTime endTime) {
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
        if(StringUtils.hasText(status)) {
            isLectureStatusEffective(status);
            queryWrapper.eq("status", status);
        }
        if(startTime != null && endTime != null) {
            queryWrapper.between("lecture_time", startTime, endTime);
        }
    }

    /**
     *  判断讲座状态status是否合法的辅助方法
     */
    private void isLectureStatusEffective(String status) {
        if(!LectureStatusConstant.LECTRUE_STATUS_SET.contains(status)) {
            throw new BaseException(ExceptionEnum.LECTURE_STATUS_INVALID);
        }
    }

    /**
     * 判断LectureUpdateDto中是否有字段需要更新
     */
    private boolean isDtoEffective(LectureUpdateDto dto) {
        return StringUtils.hasText(dto.getTitle()) ||
                StringUtils.hasText(dto.getDescription()) ||
                StringUtils.hasText(dto.getLocation()) ||
                StringUtils.hasText(dto.getTags()) ||
                StringUtils.hasText(dto.getResourceUrl()) ||
                StringUtils.hasText(dto.getRagDocId()) ||
                dto.getCapacity() != null ||
                dto.getLectureTime() != null;
    }
}
