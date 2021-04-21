package com.example.springbootshiro.mapper;

import com.example.springbootshiro.domain.UserEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author lyw
 * @Create 2021-04-21 16:27
 */
@Mapper
public interface UserMapper{

    UserEntity findUserByUserName(@Param("userName") String userName);
}
