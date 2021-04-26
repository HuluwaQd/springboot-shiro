package com.example.springbootshiro.filter;

import com.example.springbootshiro.business.system.domain.UserEntity;
import com.example.springbootshiro.business.system.service.UserService;
import com.example.springbootshiro.exception.BusinessException;
import com.example.springbootshiro.exception.FilterExceptionUtil;
import com.example.springbootshiro.utils.ApplicationContextUtil;
import com.example.springbootshiro.utils.JwtTokenUtil;
import com.example.springbootshiro.utils.RedisUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;

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


        // =================接口请求验证=================

        // 1.判断有无token
        if (StringUtils.isBlank(token)){
            FilterExceptionUtil.sendErrorToController(new BusinessException("没有token"),(HttpServletRequest) request,(HttpServletResponse) response);
            return false;
        }

        // 2.验证token在redis中是否过期
        Subject subject = SecurityUtils.getSubject();
        String principal = (String)subject.getPrincipal();
        Object redisValue = redisUtil.get(principal);
        if( redisValue == null){
            FilterExceptionUtil.sendErrorToController(new BusinessException("token过期"),(HttpServletRequest) request,(HttpServletResponse) response);
            return false;
        }

        // 3.验证token,与redis中token是否一致，token取出的userName是否相同
        String redisToken = (String)redisValue;
        UserEntity user = userService.findUserByUserName(principal);
        if(!redisToken.equals(token) || !jwtTokenUtil.validateToken(token,user)){
            FilterExceptionUtil.sendErrorToController(new BusinessException("token错误"),(HttpServletRequest) request,(HttpServletResponse) response);
            return false;
        }

        // 4.验证通过，刷新redis过期时间
        Long time = 60L*5;
        redisUtil.expire(principal,time);

        // 5.过滤器放行
        return true;

    }


    private String getRequestToken(HttpServletRequest request){
        //默认从请求头中获得token
        String token = request.getHeader(JwtTokenUtil.TOKEN_HEADER);
        return token;
    }
}
