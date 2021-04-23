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

        // 判断是否已经登陆
        if(redisUtil.get(user.getUserName()) != null){
            throw new BusinessException("该账号已经在别处登陆，请先下线");
        }

        // 没有登陆进行登陆
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(user.getUserName(), user.getPassword());
        try {
            subject.login(usernamePasswordToken);
            UserEntity userByUserName = userService.findUserByUserName(user.getUserName());
            String token = jwtTokenUtil.generateToken(userByUserName.getUserName());
            // 存入redis
            redisUtil.set(user.getUserName(),token);
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
