package com.example.springbootshiro.config;

import com.example.springbootshiro.filter.TokenFilter;
import com.example.springbootshiro.realm.UserRealm;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author lyw
 * @Create 2021-04-21 15:55
 */
@Configuration
public class ShiroConfig {
    @Bean
    public DefaultWebSecurityManager initSecurityManager(Realm realm){
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(realm);
        return securityManager;
    }

    @Bean
    public Realm initUserRealm(){
        // 修改凭证校验匹配器为MD5方式
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
        hashedCredentialsMatcher.setHashAlgorithmName("MD5");
        hashedCredentialsMatcher.setHashIterations(ShiroUtils.hashIterations);
        UserRealm userRealm = new UserRealm();
        userRealm.setCredentialsMatcher(hashedCredentialsMatcher);
        return userRealm;
    }

    @Bean("shiroFilter")
    public ShiroFilterFactoryBean shiroFilter(DefaultWebSecurityManager defaultWebSecurityManager) {
        ShiroFilterFactoryBean shiroFilter = new ShiroFilterFactoryBean();
        shiroFilter.setSecurityManager(defaultWebSecurityManager);

        //加入自定义的filter
        Map<String, Filter> filterMap = shiroFilter.getFilters();
        filterMap.put("client", new TokenFilter());
        shiroFilter.setFilters(filterMap);
        shiroFilter.setUnauthorizedUrl("/");

        // 过滤URL等
        Map<String, String> urlMap = new LinkedHashMap<>();
        urlMap.put("/swagger/**", "anon");
        urlMap.put("/v2/api-docs", "anon");
        urlMap.put("/swagger-ui.html", "anon");
        urlMap.put("/webjars/**", "anon");
        urlMap.put("/swagger-resources/**", "anon");

        urlMap.put("/login.html", "anon");
        urlMap.put("/user/login", "anon");
        urlMap.put("/user/register", "anon");
        urlMap.put("/favicon.ico", "anon");
        urlMap.put("/captcha.jpg", "anon");

        urlMap.put("/**", "authc");
        shiroFilter.setFilterChainDefinitionMap(urlMap);

        return shiroFilter;
    }

}
