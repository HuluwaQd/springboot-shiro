package com.example.springbootshiro.business.system.controller;

import com.example.springbootshiro.business.system.domain.UserEntity;
import com.example.springbootshiro.business.system.service.UserService;
import com.example.springbootshiro.common.HttpResult;
import com.example.springbootshiro.config.RedisConfig;
import com.example.springbootshiro.exception.CodeConstant;
import com.example.springbootshiro.exception.BusinessException;
import com.example.springbootshiro.utils.JwtTokenUtil;
import com.example.springbootshiro.utils.RedisUtil;
import com.example.springbootshiro.utils.ShiroUtils;
import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.Producer;
import io.swagger.annotations.Api;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
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
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private Producer producer;

    /**
     * 验证码
     * @param response
     * @throws IOException
     */
    @GetMapping("/captcha.jpg")
    public void captcha(HttpServletResponse response)throws IOException {
        response.setHeader("Cache-Control", "no-store, no-cache");
        response.setContentType("image/jpeg");

        //生成文字验证码
        String text = producer.createText();
        //生成图片验证码
        BufferedImage image = producer.createImage(text);
        //保存到shiro session
        ShiroUtils.setSessionAttribute(Constants.KAPTCHA_SESSION_KEY, text);

        ServletOutputStream out = response.getOutputStream();
        ImageIO.write(image, "jpg", out);
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
    public HttpResult login(@RequestBody UserEntity user, HttpServletRequest request){

        Subject subject = SecurityUtils.getSubject();
        return login(subject,user);
    }


    @PostMapping("/logout")
    public HttpResult logout(){
        try {
            Subject subject = SecurityUtils.getSubject();
            redisUtil.delete((String)subject.getPrincipal());
            subject.logout();
            return HttpResult.result(CodeConstant.BUSINESS_SUCCESS_CODE,"退出成功",null);
        }catch (Exception e){
            return HttpResult.result(CodeConstant.BUSINESS_ERROR_CODE,"退出失败",null);
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
            // 3.存入redis，设置默认过期时间(秒)
            redisUtil.set(user.getUserName(),token,RedisConfig.EXPIRE_TIME);
            // 4.返回token
            return HttpResult.result(CodeConstant.BUSINESS_SUCCESS_CODE, "登陆成功", token);
        }catch (UnknownAccountException e) {
            throw new BusinessException("用户不存在",CodeConstant.SYS_UNAUTHORIZED);
        }catch (IncorrectCredentialsException e) {
            throw new BusinessException("账号或密码不正确",CodeConstant.SYS_UNAUTHORIZED);
        }catch (LockedAccountException e) {
            throw new BusinessException("账号已被锁定,请联系管理员",CodeConstant.SYS_UNAUTHORIZED);
        }catch (AuthenticationException e) {
            throw new BusinessException("账号验证失败",CodeConstant.SYS_UNAUTHORIZED);
        }
    }





}
