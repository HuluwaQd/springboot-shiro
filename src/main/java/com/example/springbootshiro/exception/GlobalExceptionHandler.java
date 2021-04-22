package com.example.springbootshiro.exception;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.support.hsf.HSFJSONUtils;
import com.example.springbootshiro.common.HttpResult;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * @author lyw
 * @Create 2021-04-22 16:29
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public HttpResult globalException(BusinessException exception){
        String message = exception.getMessage();
        HttpResult httpResult = new HttpResult();
        httpResult.setMsg(message);
        httpResult.setCode(BusinessException.BUSINESS_ERROR_CODE);
        return httpResult;
    }
}

