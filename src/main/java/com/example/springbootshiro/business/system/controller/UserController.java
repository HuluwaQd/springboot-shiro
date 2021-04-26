package com.example.springbootshiro.business.system.controller;

import com.example.springbootshiro.business.system.domain.UserEntity;
import com.example.springbootshiro.business.system.service.UserService;
import com.example.springbootshiro.common.HttpResult;
import com.example.springbootshiro.exception.CodeConstant;
import com.example.springbootshiro.utils.JwtTokenUtil;
import com.example.springbootshiro.utils.RedisUtil;
import io.swagger.annotations.Api;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * @author lyw
 * @Create 2021-04-21 16:05
 */
@RestController
@RequestMapping("/sys")
@Api("登陆相关")
public class UserController {

    @Autowired
    private UserService userService;


    /**
     * 验证码
     * @param response
     * @throws IOException
     */
    @GetMapping("/captcha.jpg")
    public void captcha(HttpServletResponse response) {
        userService.captcha(response);
    }

    /**
     * 注册
     * @param user
     * @return
     */
    @PostMapping("/register")
    public HttpResult register(@RequestBody @Validated UserEntity user){
        userService.register(user);
        return HttpResult.result(CodeConstant.BUSINESS_SUCCESS_CODE, "注册成功", null);
    }

    /**
     * 登录
     * @param user
     * @param
     * @return
     */
    @PostMapping("/login")
    public HttpResult login(@RequestBody UserEntity user){
        String token = userService.login(user);
        return HttpResult.result(CodeConstant.BUSINESS_SUCCESS_CODE, "登陆成功", token);
    }

    /**
     * 登出
     * @return
     */
    @PostMapping("/logout")
    public HttpResult logout(){
        userService.logout();
        return HttpResult.result(CodeConstant.BUSINESS_SUCCESS_CODE,"退出成功",null);
    }





}
