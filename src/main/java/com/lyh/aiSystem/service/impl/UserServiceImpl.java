package com.lyh.aiSystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lyh.aiSystem.constant.JwtClaimsConstant;
import com.lyh.aiSystem.dto.UserLoginDto;
import com.lyh.aiSystem.dto.UserRegisterDto;
import com.lyh.aiSystem.dto.UserUpdateDto;
import com.lyh.aiSystem.entity.User;
import com.lyh.aiSystem.enumeration.ExceptionEnum;
import com.lyh.aiSystem.exception.BaseException;
import com.lyh.aiSystem.mapper.UserMapper;
import com.lyh.aiSystem.service.UserService;
import com.lyh.aiSystem.utils.JwtUtil;
import com.lyh.aiSystem.utils.MD5Util;
import com.lyh.aiSystem.utils.UserContextUtil;
import com.lyh.aiSystem.vo.UserDetailVo;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author BigHH
 */
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    // 使用构造函数注入
    private final UserMapper userMapper;

    private final MD5Util md5Util;

    private final JwtUtil jwtUtil;

    private final UserContextUtil userContextUtil;

//    public UserServiceImpl(UserMapper userMapper, MD5Util md5Util, JwtUtil jwtUtil, HttpServletRequest request) {
//        this.userMapper = userMapper;
//        this.md5Util = md5Util;
//        this.jwtUtil = jwtUtil;
//        this.request = request;
//    }

    /**
     *  用户注册
     * @param userRegisterDto
     */
    @Override
    public void userRegister(UserRegisterDto userRegisterDto) {
        // 先查询用户是否已存在
        User user = userMapper.selectOne(new QueryWrapper<User>().eq("username", userRegisterDto.getUsername()));
        if(user != null) { // 抛出用户已存在异常
            throw new BaseException(ExceptionEnum.USER_ALREADY_EXIST);
        }

        // 对密码进行加密
        String md5Password;
        try {
            md5Password = md5Util.getMD5Str(userRegisterDto.getPassword());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        user = User.builder()
                .username(userRegisterDto.getUsername())
                .password(md5Password)
                .build();
        int insertResult = userMapper.insert(user);
        if(insertResult == 0) { // 抛出数据库插入失败异常
            throw new BaseException(ExceptionEnum.DB_INSERT_ERROR);
        }
    }

    /**
     *  用户登录
     * @param userLoginDto
     */
    @Override
    public String userLogin(UserLoginDto userLoginDto) {
        // 对密码进行MD5加密
        String md5Password;
        try {
            md5Password = md5Util.getMD5Str(userLoginDto.getPassword());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        User user = userMapper.selectOne(new QueryWrapper<User>().eq("username", userLoginDto.getUsername()).eq("password", md5Password));
        if(user == null) { // 用户名或密码错误
            throw new BaseException(ExceptionEnum.USER_USERNAME_OR_PASSWORD_ERROR);
        }

        // 生成Jwt
        Map<String, Object> dataMap = new HashMap<>();
        // 添加自定义数据到Jwt
        dataMap.put(JwtClaimsConstant.USER_ID, user.getId()); // 用户ID
        dataMap.put(JwtClaimsConstant.USER_NAME, user.getUsername()); // 用户名
        // 返回生成的Jwt
        return jwtUtil.generateJwt(dataMap);
    }

    /**
     *  更新用户信息
     * @param userUpdateDto
     */
    @Override
    public void updateUserInfo(UserUpdateDto userUpdateDto) {
//        User user = userMapper.selectById(getCurrentUserId());
//        if(user == null) {
//            throw new BaseException(ExceptionEnum.USER_NOT_LOGIN); // 用户未登录
//        }
        // Dto中的字段都为空直接返回
        if(!isDtoEffective(userUpdateDto)) {
            return;
        }

        // 如果要修改密码，则进行加密
        if(StringUtils.hasText(userUpdateDto.getPassword())) {
            try {
                userUpdateDto.setPassword(md5Util.getMD5Str(userUpdateDto.getPassword()));
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            }
        }

        User updateUser = new User();
        // 设置属性
        BeanUtils.copyProperties(userUpdateDto, updateUser);
        updateUser.setId(userContextUtil.getUserId());
        // 更新数据库
        int updateResult = userMapper.updateById(updateUser);
        if(updateResult == 0) {
            throw new BaseException(ExceptionEnum.DB_UPDATE_ERROR);
        }
    }

    /**
     *  获取当前登录用户的详情信息
     * @return
     */
    @Override
    public UserDetailVo getUserDetail() {
        User user = userMapper.selectById(userContextUtil.getUserId());
        UserDetailVo userDetailVo = new UserDetailVo();
        BeanUtils.copyProperties(user, userDetailVo);
        // 返回用户详细信息视图对象
        return userDetailVo;
    }

    /**
     *  判断UserUpdateDto是否有字段不为空
     */
    private boolean isDtoEffective(UserUpdateDto dto) {
        return StringUtils.hasText(dto.getPassword()) ||
               StringUtils.hasText(dto.getEmail()) ||
               StringUtils.hasText(dto.getPhone()) ||
//               StringUtils.hasText(dto.getAvatarUrl()) ||
               dto.getGender() != null ||
               dto.getStatus() != null;
    }
}
