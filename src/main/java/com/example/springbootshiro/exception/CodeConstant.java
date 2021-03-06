package com.example.springbootshiro.exception;

import org.springframework.http.HttpStatus;

/**
 * 状态编码常量
 * @author lyw
 * @Create 2021-04-22 17:21
 */
public class CodeConstant {
    public final static Integer BUSINESS_SUCCESS_CODE = HttpStatus.OK.value();
    public final static Integer BUSINESS_ERROR_CODE = HttpStatus.INTERNAL_SERVER_ERROR.value();
    public final static Integer SYS_UNAUTHORIZED = HttpStatus.UNAUTHORIZED.value();
}
