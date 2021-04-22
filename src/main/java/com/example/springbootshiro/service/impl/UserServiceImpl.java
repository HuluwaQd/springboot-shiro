package com.example.springbootshiro.service.impl;

import com.example.springbootshiro.utils.ShiroUtils;
import com.example.springbootshiro.domain.UserEntity;
import com.example.springbootshiro.mapper.UserMapper;
import com.example.springbootshiro.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author lyw
 * @Create 2021-04-21 16:06
 */
@Service
@Transactional
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

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
        userMapper.register(user);
    }
}
