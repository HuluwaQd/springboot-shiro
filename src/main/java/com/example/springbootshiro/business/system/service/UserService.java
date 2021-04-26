package com.example.springbootshiro.business.system.service;

import com.example.springbootshiro.business.system.domain.UserEntity;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author lyw
 * @Create 2021-04-21 16:06
 */
public interface UserService {
    /**
     * 查找用户
     * @param userName
     * @return
     */
    UserEntity findUserByUserName(String userName);

    /**
     * 注册
     * @param user
     */
    void register(UserEntity user);

    /**
     * 验证码
     * @param response
     */
    void captcha(HttpServletResponse response);

    /**
     * 登陆
     * @param user
     * @return
     */
    String login(UserEntity user);

    /**
     * 登出
     */
    void logout();
}
