package com.example.springbootshiro.business.system.domain;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author lyw
 * @Create 2021-04-21 16:21
 */
@Data
public class UserEntity {
    private Long userId;
    @NotBlank
    private String userName;
    @NotBlank
    private String password;
    private String salt;
    private String yzm;
}
