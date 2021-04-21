package com.example.springbootshiro.controller;

import com.example.springbootshiro.domain.UserEntity;
import com.example.springbootshiro.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author lyw
 * @Create 2021-04-21 16:05
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public String login(@RequestBody UserEntity user){
        UserEntity userEntity = userService.findUserByUserName(user.getUserName());

        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(user.getUserName(), user.getPassword());
        try {
            subject.login(usernamePasswordToken);
            return "登陆成功";
        }catch (UnknownAccountException e) {
            return "用户不存在";
        }catch (IncorrectCredentialsException e) {
            return "账号或密码不正确";
        }catch (LockedAccountException e) {
            return "账号已被锁定,请联系管理员";
        }catch (AuthenticationException e) {
            return "账户验证失败";
        }
    }





}
