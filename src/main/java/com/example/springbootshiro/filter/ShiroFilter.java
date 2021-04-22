package com.example.springbootshiro.filter;

import com.example.springbootshiro.domain.UserEntity;
import com.example.springbootshiro.service.UserService;
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
 * @author lyw
 * @Create 2021-04-22 13:49
 */
public class ShiroFilter extends FormAuthenticationFilter {

    //加密的字符串,相当于签名
    private static final String SINGNATURE_TOKEN = "加密token";

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

        //如果为登录,就放行
        if ("/user/login".equals(login)||"/user/register".equals(login)){
            return true;
        }
        if (StringUtils.isBlank(token)){
            System.out.println("没有token");
            return false;
        }

        //从当前shiro中获得用户信息
        Subject subject = SecurityUtils.getSubject();
        String principal = (String)subject.getPrincipal();
        UserService userService = (UserService)ApplicationContextUtil.getBean("userServiceImpl");
        UserEntity user = userService.findUserByUserName(principal);
        JwtTokenUtil jwtTokenUtil = (JwtTokenUtil) ApplicationContextUtil.getBean("jwtTokenUtil");

        return jwtTokenUtil.validateToken(token,user);
    }
    private String getRequestToken(HttpServletRequest request){
        //默认从请求头中获得token
        String token = request.getHeader("Authorization");
        return token;
    }
}
