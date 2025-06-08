package com.lyh.aiSystem.service;

import com.lyh.aiSystem.pojo.dto.UserLoginDto;
import com.lyh.aiSystem.pojo.dto.UserRegisterDto;
import com.lyh.aiSystem.pojo.dto.UserUpdateDto;
import com.lyh.aiSystem.pojo.vo.UserDetailVo;

public interface UserService {


    void userRegister(UserRegisterDto userRegisterDto);

    String userLogin(UserLoginDto userLoginDto);

    void updateUserInfo(UserUpdateDto userUpdateDto);

    UserDetailVo getUserDetail();
}
