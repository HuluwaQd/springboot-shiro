package com.example.springbootshiro.service.impl;

import com.example.springbootshiro.domain.UserEntity;
import com.example.springbootshiro.mapper.UserMapper;
import com.example.springbootshiro.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author lyw
 * @Create 2021-04-21 16:06
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public UserEntity findUserByUserName(String userName) {
        return userMapper.findUserByUserName(userName);
    }
}
