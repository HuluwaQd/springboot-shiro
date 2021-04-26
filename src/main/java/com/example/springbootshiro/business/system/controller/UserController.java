package com.example.springbootshiro.business.system.controller;

import com.example.springbootshiro.business.system.domain.UserEntity;
import com.example.springbootshiro.business.system.service.UserService;
import com.example.springbootshiro.common.HttpResult;
import com.example.springbootshiro.exception.BusinessConstant;
import com.example.springbootshiro.exception.BusinessException;
import com.example.springbootshiro.utils.JwtTokenUtil;
import com.example.springbootshiro.utils.RedisUtil;
import io.swagger.annotations.Api;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
    @Autowired
    private RedisUtil redisUtil;

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
        return login(subject,user);
    }


    @PostMapping("/logout")
    public String logout(){
        try {
            Subject subject = SecurityUtils.getSubject();
            redisUtil.delete((String)subject.getPrincipal());
            subject.logout();
            return "退出成功";
        }catch (Exception e){
            return "退出失败";
        }

    }

    /**
     * 登陆
     * @param subject
     * @param user
     * @return
     */
    public HttpResult login(Subject subject,UserEntity user){

        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(user.getUserName(), user.getPassword());
        try {
            // 1.验证
            subject.login(usernamePasswordToken);
            // 2.验证通过，生成新token
            UserEntity userByUserName = userService.findUserByUserName(user.getUserName());
            String token = jwtTokenUtil.generateToken(userByUserName.getUserName());
            // 3.存入redis，设置默认过期时间(秒) 5分钟
            Long time = 60L*5;
            redisUtil.set(user.getUserName(),token,time);
            // 4.返回token
            return HttpResult.result(BusinessConstant.BUSINESS_SUCCESS_CODE, "登陆成功", token);
        }catch (UnknownAccountException e) {
            throw new BusinessException("用户不存在");
        }catch (IncorrectCredentialsException e) {
            throw new BusinessException("账号或密码不正确");
        }catch (LockedAccountException e) {
            throw new BusinessException("账号已被锁定,请联系管理员");
        }catch (AuthenticationException e) {
            throw new BusinessException("账号验证失败");
        }
    }





}
