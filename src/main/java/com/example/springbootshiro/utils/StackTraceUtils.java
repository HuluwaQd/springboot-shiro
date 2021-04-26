package com.example.springbootshiro.utils;

import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * 堆栈异常打印
 * @author wangzb
 * @version 1.0
 * @date 2021/01/07
 */
@Slf4j
public class StackTraceUtils {
    private StackTraceUtils(){}
    public static void print(Exception e){
        Arrays.stream(e.getStackTrace()).collect(Collectors.toList()).forEach(s->{
            if (log.isErrorEnabled()){
                log.warn("[{}]: method:{},line:{}",s.getClassName(),s.getMethodName(),s.getLineNumber());
            }
        });
    }
}
