package com.lyh.aiSystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lyh.aiSystem.constant.AdminRoleConstant;
import com.lyh.aiSystem.constant.JwtClaimsConstant;
import com.lyh.aiSystem.enumeration.ExceptionEnum;
import com.lyh.aiSystem.exception.BaseException;
import com.lyh.aiSystem.mapper.AdminMapper;
import com.lyh.aiSystem.pojo.dto.AdminCreateDto;
import com.lyh.aiSystem.pojo.dto.AdminLoginDto;
import com.lyh.aiSystem.pojo.dto.AdminUpdateDto;
import com.lyh.aiSystem.pojo.entity.Admin;
import com.lyh.aiSystem.service.AdminService;
import com.lyh.aiSystem.utils.AdminContextUtil;
import com.lyh.aiSystem.utils.JwtUtil;
import com.lyh.aiSystem.utils.MD5Util;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


/**
 * @author BigHH
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class AdminServiceImpl implements AdminService {

    private final AdminMapper adminMapper;

    private final MD5Util md5Util;

    private final JwtUtil jwtUtil;

    private final AdminContextUtil adminContextUtil;

    /**
     *  管理员登录
     * @param adminLoginDto
     * @return
     */
    @Override
    public String adminLogin(AdminLoginDto adminLoginDto) {
        // 对用户输入密码进行MD5加密
        String md5Password;
        try {
            md5Password = md5Util.getMD5Str(adminLoginDto.getPassword());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        // 查数据库
        Admin admin = adminMapper.selectOne(new QueryWrapper<Admin>().eq("username", adminLoginDto.getUsername()).eq("password", md5Password));
        if(admin == null) {
            throw new BaseException(ExceptionEnum.ADMIN_USERNAME_OR_PASSWORD_ERROR); // 抛出用户名或密码错误异常
        }

        // 生成Jwt
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put(JwtClaimsConstant.ADMIN_ID, admin.getId());
        dataMap.put(JwtClaimsConstant.ADMIN_ROLE, admin.getRole());
        dataMap.put(JwtClaimsConstant.ADMIN_NAME, admin.getUsername());
        // 返回生成的Jwt
        return jwtUtil.generateJwt(dataMap);
    }

    /**
     *  修改管理员信息
     * @param adminUpdateDto
     */
    @Override
    public void updateAdminInfo(AdminUpdateDto adminUpdateDto) {
        // 判断Dto中的字段是否全为空
        if(!isDtoEffective(adminUpdateDto)) {
            return; // 如果Dto中的字段全为空，直接返回
        }

        // 判断是否要修改密码
        if(StringUtils.hasText(adminUpdateDto.getPassword())) {
            try {
                adminUpdateDto.setPassword(md5Util.getMD5Str(adminUpdateDto.getPassword()));
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            }
        }

        Admin admin = new Admin();
        BeanUtils.copyProperties(adminUpdateDto, admin);
        admin.setId(adminContextUtil.getAdminId());
        // 更新数据库
        int updateResult = adminMapper.updateById(admin);
        if(updateResult == 0) {
            throw new BaseException(ExceptionEnum.DB_UPDATE_ERROR);
        }
    }

    /**
     *  创建普通管理员
     * @param adminCreateDto
     */
    @Override
    public void createAdmin(AdminCreateDto adminCreateDto) {
        // 判断当前登录用户是否是超级管理员
        log.debug("Role:{}", adminContextUtil.getAdminRole());
        if(!Objects.equals(adminContextUtil.getAdminRole(), AdminRoleConstant.ADMIN_ROLE_SUPER_ADMIN)) {
            throw new BaseException(ExceptionEnum.ADMIN_NOT_SUPPER_ADMIN); // 当前登录用户不是超级管理员，抛出异常
        }

        // 判断要创建的管理员是否已存在
        Admin admin = adminMapper.selectOne(new QueryWrapper<Admin>().eq("username", adminCreateDto.getUsername()));
        if(admin != null) {
            throw new BaseException(ExceptionEnum.ADMIN_ALREADY_EXIST); // 抛出管理员已存在异常
        }

        // 对密码进行MD5加密
        try {
            adminCreateDto.setPassword(md5Util.getMD5Str(adminCreateDto.getPassword()));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        // 创建管理员
        admin = new Admin();
        BeanUtils.copyProperties(adminCreateDto, admin);
        int insertResult = adminMapper.insert(admin);
        if(insertResult == 0) {
            throw new BaseException(ExceptionEnum.DB_INSERT_ERROR); // 抛出数据库插入失败异常
        }
    }

    /**
     *  判断AdminUpdateDto是否有字段不为空
     */
    public boolean isDtoEffective(AdminUpdateDto adminUpdateDto) {
        return StringUtils.hasText(adminUpdateDto.getPassword()) ||
                StringUtils.hasText(adminUpdateDto.getEmail()) ||
                StringUtils.hasText(adminUpdateDto.getPhone()) ||
                adminUpdateDto.getGender() != null;
    }
}
