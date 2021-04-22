package com.example.springbootshiro.exception;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
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

    /**
     * 请求信息异常捕获
     * @param exception
     * @param request
     * @return
     */
    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public JSON validationBindException(BindException exception, HttpServletRequest request) {
        BindingResult result = exception.getBindingResult();
        return formatValidationException(result, request);
    }

    private JSON formatValidationException(BindingResult result, HttpServletRequest request) {
        JSONObject json = new JSONObject();
        StringBuffer errBuffer = new StringBuffer();
        StringBuffer msgBuffer = new StringBuffer();
        if (result.hasErrors()) {
            List<ObjectError> errors = result.getAllErrors();
            errors.forEach(p -> {
                FieldError fieldError = (FieldError) p;
                if (errBuffer.length() > 0) {
                    errBuffer.append("; ");
                }
                if (msgBuffer.length() > 0) {
                    msgBuffer.append("; ");
                }
                errBuffer.append("The value [");
                errBuffer.append(fieldError.getRejectedValue());
                errBuffer.append("] of { ");
                errBuffer.append(fieldError.getObjectName());
                errBuffer.append(".");
                errBuffer.append(fieldError.getField());
                errBuffer.append(" } does not conform to the specification { ");
                errBuffer.append(fieldError.getCode());
                errBuffer.append(" }");
                msgBuffer.append(fieldError.getDefaultMessage());
            });
            json.put("status", HttpStatus.BAD_REQUEST.value());
            json.put("error", errBuffer.toString());
            json.put("message", msgBuffer.toString());
            json.put("path", request.getServletPath());
            json.put("timestamp", new Date());
        }
        return json;
    }
}

