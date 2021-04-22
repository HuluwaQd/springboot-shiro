package com.example.springbootshiro.common;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 请求统一返回
 * @author lyw
 * @Create 2021-04-22 16:44
 */
@Accessors(chain = true)
@Data
public class HttpResult {
    private Integer code;
    private String msg;
    private Object data;

    public static HttpResult result(Integer code,String msg,Object data){
        return new HttpResult().setCode(code).setMsg(msg).setData(data);
    }
}
