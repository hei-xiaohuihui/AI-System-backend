package com.lyh.aiSystem.service;

import com.lyh.aiSystem.dto.UserLoginDto;
import com.lyh.aiSystem.dto.UserRegisterDto;
import com.lyh.aiSystem.dto.UserUpdateDto;
import com.lyh.aiSystem.vo.UserDetailVo;

public interface UserService {


    void userRegister(UserRegisterDto userRegisterDto);

    String userLogin(UserLoginDto userLoginDto);

    void updateUserInfo(UserUpdateDto userUpdateDto);

    UserDetailVo getUserDetail();
}
