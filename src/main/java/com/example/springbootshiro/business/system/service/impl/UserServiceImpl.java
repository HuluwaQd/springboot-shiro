package com.example.springbootshiro.business.system.service.impl;

import com.example.springbootshiro.common.HttpResult;
import com.example.springbootshiro.config.RedisConfig;
import com.example.springbootshiro.config.snowflake.IDGenerator;
import com.example.springbootshiro.exception.BusinessException;
import com.example.springbootshiro.exception.CodeConstant;
import com.example.springbootshiro.utils.JwtTokenUtil;
import com.example.springbootshiro.utils.RedisUtil;
import com.example.springbootshiro.utils.ShiroUtils;
import com.example.springbootshiro.business.system.domain.UserEntity;
import com.example.springbootshiro.business.system.mapper.UserMapper;
import com.example.springbootshiro.business.system.service.UserService;
import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.Producer;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * @author lyw
 * @Create 2021-04-21 16:06
 */
@Service
@Transactional
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private IDGenerator idGenerator;
    @Autowired
    private Producer producer;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private RedisUtil redisUtil;

    @Override
    public UserEntity findUserByUserName(String userName) {
        return userMapper.findUserByUserName(userName);
    }

    @Override
    public void register(UserEntity user) {
        String salt = ShiroUtils.randomSalt();
        String md5Password = ShiroUtils.MD5(user.getPassword(), salt);
        user.setPassword(md5Password);
        user.setSalt(salt);
        user.setUserId(idGenerator.nextId());
        userMapper.register(user);
    }

    /**
     * 验证码
     *
     * @param response
     */
    @Override
    public void captcha(HttpServletResponse response) {
        try {
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
        }catch (IOException e){
            throw new BusinessException("验证码获取失败");
        }

    }

    /**
     * 登陆
     *
     * @param user
     * @return
     */
    @Override
    public String login(UserEntity user) {
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(user.getUserName(), user.getPassword());
        try {
            // 1.验证
            Object sessionAttribute = ShiroUtils.getSessionAttribute(Constants.KAPTCHA_SESSION_KEY);
            if(sessionAttribute == null){
                throw new BusinessException("验证码异常");
            }
            String yzm = (String) sessionAttribute;
            if (!yzm.equals(user.getYzm())) {
                throw new BusinessException("验证码不正确");
            }
            Subject subject = SecurityUtils.getSubject();
            subject.login(usernamePasswordToken);
            // 2.验证通过，生成新token
            UserEntity userByUserName = findUserByUserName(user.getUserName());
            String token = jwtTokenUtil.generateToken(userByUserName.getUserName());
            // 3.存入redis，设置默认过期时间(秒)
            redisUtil.set(user.getUserName(),token, RedisConfig.EXPIRE_TIME);
            // 4.返回token
            return token;
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

    /**
     * 登出
     */
    @Override
    public void logout() {
        try {
            Subject subject = SecurityUtils.getSubject();
            redisUtil.delete((String)subject.getPrincipal());
            subject.logout();
        }catch (Exception e){
            throw new BusinessException("退出登陆失败",CodeConstant.BUSINESS_ERROR_CODE);
        }
    }
}
