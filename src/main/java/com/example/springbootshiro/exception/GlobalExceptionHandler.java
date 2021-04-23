package com.example.springbootshiro.exception;

import com.example.springbootshiro.common.HttpResult;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理
 * @author lyw
 * @Create 2021-04-22 16:29
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public HttpResult globalBusinessException(BusinessException exception){
        String message = exception.getMessage();
        HttpResult httpResult = new HttpResult();
        httpResult.setMsg(message);
        httpResult.setCode(BusinessException.BUSINESS_ERROR_CODE);
        return httpResult;
    }
}

