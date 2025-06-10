package com.lyh.aiSystem.controller;

import com.lyh.aiSystem.pojo.dto.UserLoginDto;
import com.lyh.aiSystem.pojo.dto.UserRegisterDto;
import com.lyh.aiSystem.pojo.dto.UserUpdateDto;
import com.lyh.aiSystem.service.UserService;
import com.lyh.aiSystem.utils.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author BigHH
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/user/auth")
public class UserController {

    private final UserService userService;

    // 使用构造函数注入
//    public UserController(UserService userService) {
//        this.userService = userService;
//    }

    /**
     *  用户注册
     * @param userRegisterDto
     * @return
     */
    @PostMapping("/register")
    public Result userResister(@RequestBody @Valid UserRegisterDto userRegisterDto) {
        userService.userRegister(userRegisterDto);
        return Result.success();
    }

    /**
     *  用户登录
     * @param userLoginDto
     * @return
     */
    @PostMapping("/login")
    public Result userLogin(@RequestBody @Valid UserLoginDto userLoginDto) {
        return Result.success(userService.userLogin(userLoginDto));
    }

    /**
     *  获取当前用户的详细信息
     * @return
     */
    @GetMapping("/detail")
    public Result getUserDetail() {
        return Result.success(userService.getUserDetail());
    }

    /**
     *  修改用户信息
     * @return
     */
    @PostMapping("/update")
    public Result updateUserInfo(@RequestBody @Valid UserUpdateDto userUpdateDto) {
        userService.updateUserInfo(userUpdateDto);
        return Result.success();
    }

    /**
     *  检测token是否过期：只要前端成功请求到了这个接口，说明token没有过期（若过期了会被JwtInterceptor拦截）
     * @return
     */
    @GetMapping("/checkToken")
    public Result checkToken() {
        return Result.success();
    }

//    @GetMapping("/test")
//    public Result test() {
//        return Result.success();
//    }
}
