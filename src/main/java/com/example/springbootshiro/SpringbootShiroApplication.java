package com.example.springbootshiro;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@MapperScan({"com.example.springbootshiro.business.system.mapper"})
@Slf4j
public class SpringbootShiroApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringbootShiroApplication.class, args);
        log.info("接口文档地址:localhost:/shiro-test/doc.html");
    }

}
