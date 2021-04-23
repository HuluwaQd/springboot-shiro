package com.example.springbootshiro.exception;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 过滤器异常工具类
 * @author lyw
 * @Create 2021-04-23 10:33
 */
@Slf4j
public class FilterExceptionUtil {

    public static void sendErrorToController(BusinessException e,HttpServletRequest request, HttpServletResponse response){
        // 异常捕获，发送到error controller
        request.setAttribute("filter.error", e);
        try {
            //转发异常
            request.getRequestDispatcher("/handle/error").forward(request, response);
        }catch (Exception exception){
            log.error("非业务异常(系统异常)转发失败:",exception.getMessage());
        }
    }

}
