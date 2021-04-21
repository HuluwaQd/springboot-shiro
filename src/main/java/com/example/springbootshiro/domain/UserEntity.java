package com.example.springbootshiro.domain;

import lombok.Data;

/**
 * @author lyw
 * @Create 2021-04-21 16:21
 */
@Data
public class UserEntity {
    private Long userId;
    private String userName;
    private String password;
    private String salt;
}
