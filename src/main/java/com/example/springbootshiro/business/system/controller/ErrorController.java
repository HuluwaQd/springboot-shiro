package com.example.springbootshiro.business.system.controller;

import com.example.springbootshiro.exception.BusinessException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 异常处理controller
 * @author lyw
 * @Create 2021-04-23 10:50
 */
@RestController
public class ErrorController {

    @RequestMapping("/handle/error")
    public void handleError(HttpServletRequest request) throws BusinessException {
        throw ((BusinessException) request.getAttribute("filter.error"));
    }
}
