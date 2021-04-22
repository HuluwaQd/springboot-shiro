package com.example.springbootshiro.filter;

import com.example.springbootshiro.business.system.domain.UserEntity;
import com.example.springbootshiro.business.system.service.UserService;
import com.example.springbootshiro.utils.ApplicationContextUtil;
import com.example.springbootshiro.utils.JwtTokenUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 * 所有请求走自定义过滤器
 * @author lyw
 * @Create 2021-04-22 13:49
 */
public class ShiroFilter extends FormAuthenticationFilter {

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        //这里只有返回false才会执行onAccessDenied方法,因为
        // return super.isAccessAllowed(request, response, mappedValue);
        return false;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String token = getRequestToken(httpServletRequest);
        String login = (httpServletRequest).getServletPath();

        //如果为登录,或者注册就放行
        if ("/user/login".equals(login)||"/user/register".equals(login)){
            return true;
        }
        // 没有token
        if (StringUtils.isBlank(token)){
            throw new RuntimeException("has no token");
        }

        Subject subject = SecurityUtils.getSubject();
        // 没有登陆
        if (!subject.isAuthenticated()) {
            throw new RuntimeException("not Authenticated");
        }

        // 验证token
        String principal = (String)subject.getPrincipal();
        UserService userService = (UserService)ApplicationContextUtil.getBean("userServiceImpl");
        UserEntity user = userService.findUserByUserName(principal);
        JwtTokenUtil jwtTokenUtil = (JwtTokenUtil) ApplicationContextUtil.getBean("jwtTokenUtil");
        if(!jwtTokenUtil.validateToken(token,user)){
            throw new RuntimeException("error token");
        }
        return true;
    }
    private String getRequestToken(HttpServletRequest request){
        //默认从请求头中获得token
        String token = request.getHeader(JwtTokenUtil.TOKEN_HEADER);
        return token;
    }
}
