package com.example.springbootshiro.realm;

import com.example.springbootshiro.business.system.domain.UserEntity;
import com.example.springbootshiro.business.system.service.UserService;
import com.example.springbootshiro.utils.ApplicationContextUtil;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;

/**
 * @author lyw
 * @Create 2021-04-21 16:00
 */
public class UserRealm extends AuthorizingRealm {

    /**
     * 授权
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        return null;
    }

    /**
     * 账户密码验证
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        String principal = (String)authenticationToken.getPrincipal();

        UserService userService = (UserService)ApplicationContextUtil.getBean("userServiceImpl");
        UserEntity userByUserName = userService.findUserByUserName(principal);
        if(userByUserName !=null){
            return new SimpleAuthenticationInfo(principal, userByUserName.getPassword(), ByteSource.Util.bytes(userByUserName.getSalt()),this.getName());
        }else {
            return null;
        }
    }
}
