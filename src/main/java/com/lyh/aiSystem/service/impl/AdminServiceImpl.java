package com.lyh.aiSystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lyh.aiSystem.constant.AdminRoleConstant;
import com.lyh.aiSystem.constant.JwtClaimsConstant;
import com.lyh.aiSystem.enumeration.ExceptionEnum;
import com.lyh.aiSystem.exception.BaseException;
import com.lyh.aiSystem.mapper.AdminMapper;
import com.lyh.aiSystem.mapper.UserMapper;
import com.lyh.aiSystem.pojo.dto.*;
import com.lyh.aiSystem.pojo.entity.Admin;
import com.lyh.aiSystem.pojo.entity.User;
import com.lyh.aiSystem.pojo.vo.AdminPageVo;
import com.lyh.aiSystem.pojo.vo.AdminVo;
import com.lyh.aiSystem.pojo.vo.UserPageVo;
import com.lyh.aiSystem.properties.JwtProperties;
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
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;


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

    private final JwtProperties jwtProperties;

    private final AdminContextUtil adminContextUtil;

    private final UserMapper  userMapper;

    /**
     *  管理员登录
     * @param adminLoginDto
     * @return
     */
    @Override
    public String adminLogin(AdminLoginDto adminLoginDto) {
        // 先检查是否是管理员
        // 根据用户名查询
//        Admin one = adminMapper.selectOne(new QueryWrapper<Admin>().eq("username", adminLoginDto.getUsername()));
//        if(one == null) {
//            throw new BaseException(ExceptionEnum.ADMIN_NOT_EXIST); // 抛出管理员不存在异常
//        }

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
        return jwtUtil.generateJwt(jwtProperties.getAdminSecretKey(), dataMap);
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
        if(adminUpdateDto.getId() == null) {
            admin.setId(adminContextUtil.getAdminId());
        }
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
        isSuperAdmin();

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
     *  超级管理员分页查询普通用户信息
     * @param userPageDto
     * @return
     */
    @Override
    public IPage<UserPageVo> userPage(UserPageDto userPageDto) {
        isSuperAdmin();
        // 创建分页对象
        Page<User> page = new Page<>(userPageDto.getPageNum(), userPageDto.getPageSize());

        // 创建查询条件
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if(StringUtils.hasText(userPageDto.getUsername())) {
            queryWrapper.like("username", userPageDto.getUsername());
        }
        if(StringUtils.hasText(userPageDto.getRealName())) {
            queryWrapper.like("real_name", userPageDto.getRealName());
        }
        if(StringUtils.hasText(userPageDto.getStudentId())) {
            queryWrapper.like("student_id", userPageDto.getStudentId());
        }
        if(StringUtils.hasText(userPageDto.getEmail())) {
            queryWrapper.like("email", userPageDto.getEmail());
        }
        if(StringUtils.hasText(userPageDto.getPhone())) {
            queryWrapper.like("phone", userPageDto.getPhone());
        }
        if(userPageDto.getStatus() != null) {
            queryWrapper.eq("status", userPageDto.getStatus());
        }

        // 进行分页查询
        IPage<User> userPage = userMapper.selectPage(page, queryWrapper);
        // 实体列表转为Vo列表
        List<UserPageVo> voList = userPage.getRecords().stream().map(user -> {
            UserPageVo vo = new UserPageVo();
            BeanUtils.copyProperties(user, vo);
            return vo;
        }).collect(Collectors.toList());

        // 构造分页返回对象
        Page<UserPageVo> userPageVo = new Page<>();
        userPageVo.setCurrent(userPage.getCurrent()); // 设置当前页码
        userPageVo.setSize(userPage.getSize()); // 设置每页大小
        userPageVo.setTotal(userPage.getTotal());
        userPageVo.setRecords(voList);
        return userPageVo;
    }

    /**
     *  超级管理员分页查询讲师信息
     * @param adminPageDto
     * @return
     */
    @Override
    public IPage<AdminPageVo> adminPage(AdminPageDto adminPageDto) {
        isSuperAdmin();
        // 创建分页对象
        Page<Admin> page = new Page<>(adminPageDto.getPageNum(), adminPageDto.getPageSize());
        // 创建查询条件
        QueryWrapper<Admin> queryWrapper = new QueryWrapper<>();
        if(StringUtils.hasText(adminPageDto.getLecturerName())) {
            queryWrapper.like("lecturer_name", adminPageDto.getLecturerName());
        }
        if(StringUtils.hasText(adminPageDto.getLecturerTitle())) {
            queryWrapper.like("lecturer_title", adminPageDto.getLecturerTitle());
        }
        if(StringUtils.hasText(adminPageDto.getEmail())) {
            queryWrapper.like("email", adminPageDto.getEmail());
        }
        if(StringUtils.hasText(adminPageDto.getPhone())) {
            queryWrapper.like("phone", adminPageDto.getPhone());
        }
        if(adminPageDto.getStatus() != null) {
            queryWrapper.eq("status", adminPageDto.getStatus());
        }

        // 进行分页查询
        IPage<Admin> adminPage = adminMapper.selectPage(page, queryWrapper);
        // 实体列表转为Vo列表
        List<AdminPageVo> voList = adminPage.getRecords().stream()
            .filter(admin -> !admin.getRole().equals(AdminRoleConstant.ADMIN_ROLE_SUPER_ADMIN)) // 排除超级管理员
            .map(admin -> {
                AdminPageVo vo = new AdminPageVo();
                BeanUtils.copyProperties(admin, vo);
                return vo;
        }).collect(Collectors.toList());

        // 构造分页返回对象
        Page<AdminPageVo> adminPageVo = new Page<>();
        adminPageVo.setCurrent(adminPage.getCurrent()); // 设置当前页码
        adminPageVo.setSize(adminPage.getSize()); // 设置每页大小
        adminPageVo.setTotal(adminPage.getTotal());
        adminPageVo.setRecords(voList);
        return adminPageVo;
    }

    /**
     *  超级管理员更新讲师账户状态
     * @param adminId
     * @param status
     */
    @Override
    public void updateAdminStatus(Long adminId, Byte status) {
        isSuperAdmin();

        Admin admin = adminMapper.selectOne(new QueryWrapper<Admin>().eq("id", adminId));
        if(admin == null) {
            throw new BaseException(ExceptionEnum.ADMIN_NOT_EXIST);
        }

        // 更新讲师账户状态
        admin.setStatus(status);
        int updateResult = adminMapper.updateById(admin);
        if(updateResult == 0) {
            throw new BaseException(ExceptionEnum.DB_UPDATE_ERROR);
        }
    }

    /**
     *  超级管理员更新其他管理员（讲师）信息
     * @param adminUpdateDto
     */
    @Override
    public void updateOtherAdminInfo(AdminUpdateDto adminUpdateDto) {
        if(StringUtils.hasText(adminUpdateDto.getUsername())) {
            // 判断要修改的用户名是否已存在
            Admin admin = adminMapper.selectOne(new QueryWrapper<Admin>().eq("id", adminUpdateDto.getId()));
            if(admin != null && !Objects.equals(admin.getUsername(), adminUpdateDto.getUsername())) { // 要修改的用户名为非当前用户的用户名且已存在
                throw new BaseException(ExceptionEnum.ADMIN_ALREADY_EXIST);
            }
        }

        if(adminUpdateDto.getId() != null) {
            // 根据id查询admin是否存在
            if(adminMapper.selectOne(new QueryWrapper<Admin>().eq("id", adminUpdateDto.getId())) ==  null) {
                throw new BaseException(ExceptionEnum.ADMIN_NOT_EXIST);
            }
        }

        this.updateAdminInfo(adminUpdateDto);
    }

    /**
     *  获取管理员账户详细信息
     * @return
     */
    @Override
    public AdminVo getAdminDetail() {
        Admin admin = adminMapper.selectOne(new QueryWrapper<Admin>().eq("id", adminContextUtil.getAdminId()));
        if(admin == null) {
            throw new BaseException(ExceptionEnum.ADMIN_NOT_EXIST);
        }

        AdminVo adminVo = new AdminVo();
        if(admin.getRole().equals(AdminRoleConstant.ADMIN_ROLE_LECTURER)) { // 讲师直接复制属性
            BeanUtils.copyProperties(admin, adminVo);
        } else { // 超级管理员只设置部分属性
            adminVo.setUsername(admin.getUsername());
            adminVo.setEmail(admin.getEmail());
            adminVo.setPhone(admin.getPhone());
        }
        return adminVo;
    }

    /**
     *  超级管理员更新学生账户状态
     * @param userId
     * @param status
     */
    @Override
    public void updateUserStatus(Long userId, Byte status) {
        isSuperAdmin();

        User user = userMapper.selectOne(new QueryWrapper<User>().eq("id", userId));
        if(user == null) {
            throw new BaseException(ExceptionEnum.USER_NOT_EXIST);
        }

        // 更新用户信息
        user.setStatus(status);
        int updateResult = userMapper.updateById(user);
        if(updateResult == 0) {
            throw new BaseException(ExceptionEnum.DB_UPDATE_ERROR);
        }
    }

    /**
     *  判断AdminUpdateDto是否有字段不为空
     */
    private boolean isDtoEffective(AdminUpdateDto adminUpdateDto) {
        return StringUtils.hasText(adminUpdateDto.getPassword()) ||
                StringUtils.hasText(adminUpdateDto.getLecturerName()) ||
                StringUtils.hasText(adminUpdateDto.getLecturerTitle()) ||
                StringUtils.hasText(adminUpdateDto.getEmail()) ||
                StringUtils.hasText(adminUpdateDto.getPhone()) ||
                adminUpdateDto.getGender() != null;
    }

    /**
     *  判断是否是超级管理员
     */
    private void isSuperAdmin() {
        boolean isSuperAdmin = adminContextUtil.getAdminRole().equals(AdminRoleConstant.ADMIN_ROLE_SUPER_ADMIN);
        if(!isSuperAdmin) {
            throw new BaseException(ExceptionEnum.ADMIN_NOT_SUPPER_ADMIN);
        }
    }
}
