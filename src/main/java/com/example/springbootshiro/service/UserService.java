package com.example.springbootshiro.service;

import com.example.springbootshiro.domain.UserEntity;

/**
 * @author lyw
 * @Create 2021-04-21 16:06
 */
public interface UserService {
    UserEntity findUserByUserName(String userName);
}
