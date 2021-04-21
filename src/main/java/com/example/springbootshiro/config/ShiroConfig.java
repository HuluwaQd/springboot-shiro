package com.example.springbootshiro.config;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.Authorizer;
import org.apache.shiro.authz.ModularRealmAuthorizer;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author lyw
 * @Create 2021-04-21 15:55
 */
@Configuration
public class ShiroConfig {
    @Bean
    public SecurityManager initSecurityManager(UserRealm userRealm){
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(userRealm);
        securityManager.setRememberMeManager(null);
        SecurityUtils.setSecurityManager(securityManager);
        return securityManager;
    }

    @Bean
    public UserRealm initUserRealm(){
        UserRealm userRealm = new UserRealm();
        return userRealm;
    }


    @Bean
    public Authorizer authorizer(){
        return new ModularRealmAuthorizer();
    }
}
