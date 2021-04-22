package com.example.springbootshiro.business.system.service;

import com.example.springbootshiro.business.system.domain.UserEntity;

/**
 * @author lyw
 * @Create 2021-04-21 16:06
 */
public interface UserService {
    UserEntity findUserByUserName(String userName);

    void register(UserEntity user);
}
