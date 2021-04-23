package com.example.springbootshiro.filter;

import com.example.springbootshiro.business.system.domain.UserEntity;
import com.example.springbootshiro.business.system.service.UserService;
import com.example.springbootshiro.exception.BusinessException;
import com.example.springbootshiro.exception.FilterExceptionUtil;
import com.example.springbootshiro.utils.ApplicationContextUtil;
import com.example.springbootshiro.utils.JwtTokenUtil;
import com.example.springbootshiro.utils.RedisUtil;
import javafx.application.Application;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 所有请求走自定义过滤器
 * @author lyw
 * @Create 2021-04-22 13:49
 */


public class CustomFilter extends FormAuthenticationFilter {


    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        //这里只有返回false才会执行onAccessDenied方法,因为
        // return super.isAccessAllowed(request, response, mappedValue);
        return false;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) {
        JwtTokenUtil jwtTokenUtil = (JwtTokenUtil)ApplicationContextUtil.getBean("jwtTokenUtil");
        RedisUtil redisUtil = (RedisUtil)ApplicationContextUtil.getBean("redisUtil");
        UserService userService = (UserService)ApplicationContextUtil.getBean("userServiceImpl");


        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String token = getRequestToken(httpServletRequest);
        String login = (httpServletRequest).getServletPath();

        // 注册放行
        if ("/user/register".equals(login)){
            return true;
        }
        //登录
        if ("/user/login".equals(login)){
            return true;
        }

        // 没有token
        if (StringUtils.isBlank(token)){
            FilterExceptionUtil.sendErrorToController(new BusinessException("没有token"),(HttpServletRequest) request,(HttpServletResponse) response);
            return false;
        }


        Subject subject = SecurityUtils.getSubject();
        // 异地登陆
        if (!subject.isAuthenticated()) {
            // 验证是否在redis中存在相同的token
            String usernameFromToken = jwtTokenUtil.getUsernameFromToken(token);
            String redisToken = (String)redisUtil.get(usernameFromToken);
            if (!token.equals(redisToken)){
                FilterExceptionUtil.sendErrorToController(new BusinessException("该账号已经在别处登陆，请先下线"),(HttpServletRequest) request,(HttpServletResponse) response);
                return false;
            }else {
                return true;
            }
//            FilterExceptionUtil.sendErrorToController(new BusinessException("没有登陆"),(HttpServletRequest) request,(HttpServletResponse) response);
        }else {
            // 非异地登陆，验证token
            String principal = (String)subject.getPrincipal();
            UserEntity user = userService.findUserByUserName(principal);
            // token错误
            if(!jwtTokenUtil.validateToken(token,user)){
                FilterExceptionUtil.sendErrorToController(new BusinessException("token错误"),(HttpServletRequest) request,(HttpServletResponse) response);
                return false;
            }
            return true;
        }
    }
    private String getRequestToken(HttpServletRequest request){
        //默认从请求头中获得token
        String token = request.getHeader(JwtTokenUtil.TOKEN_HEADER);
        return token;
    }
}
