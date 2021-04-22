package com.example.springbootshiro.business.system.controller;

import com.example.springbootshiro.common.HttpResult;
import com.example.springbootshiro.exception.BusinessConstant;
import com.example.springbootshiro.exception.BusinessException;
import com.example.springbootshiro.utils.JwtTokenUtil;
import com.example.springbootshiro.business.system.domain.UserEntity;
import com.example.springbootshiro.business.system.service.UserService;
import io.swagger.annotations.Api;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;


/**
 * @author lyw
 * @Create 2021-04-21 16:05
 */
@RestController
@RequestMapping("/user")
@Api("用户接口")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    /**
     * 注册
     * @param user
     * @return
     */
    @PostMapping("/register")
    public String register(@RequestBody @Validated UserEntity user){
        userService.register(user);
        return "注册成功";
    }

    /**
     * 登录
     * @param user
     * @param
     * @return
     */
    @PostMapping("/login")
    public HttpResult login(@RequestBody UserEntity user, HttpServletRequest request){

        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(user.getUserName(), user.getPassword());
        try {
            subject.login(usernamePasswordToken);
            UserEntity userByUserName = userService.findUserByUserName(user.getUserName());
            String token = jwtTokenUtil.generateToken(userByUserName.getUserName());
            return HttpResult.result(BusinessConstant.BUSINESS_SUCCESS_CODE, "登陆成功", token);
        }catch (UnknownAccountException e) {
            return HttpResult.result(BusinessConstant.BUSINESS_ERROR_CODE, "用户不存在", null);
        }catch (IncorrectCredentialsException e) {
            return HttpResult.result(BusinessConstant.BUSINESS_ERROR_CODE, "账号或密码不正确", null);
        }catch (LockedAccountException e) {
            return HttpResult.result(BusinessConstant.BUSINESS_ERROR_CODE, "账号已被锁定,请联系管理员", null);
        }catch (AuthenticationException e) {
            return HttpResult.result(BusinessConstant.BUSINESS_ERROR_CODE, "账号验证失败", null);
        }
    }

    @PostMapping("/logout")
    public String logout(){
        try {
            Subject subject = SecurityUtils.getSubject();
            subject.logout();
            return "退出成功";
        }catch (Exception e){
            return "退出失败";
        }

    }





}
