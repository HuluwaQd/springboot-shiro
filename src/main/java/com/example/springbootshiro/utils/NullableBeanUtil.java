package com.example.springbootshiro.utils;

import org.springframework.beans.BeanUtils;

import java.util.function.Supplier;

/**
 * <p>source可以为空的copy</p>
 *
 * @author HuMingAn
 * @version 1.0
 * @date 2020/07/07 10:04
 **/
public class NullableBeanUtil {
    private NullableBeanUtil() {
    }

    public static <T> T copyProperties(Object source, Supplier<T> target) {
        T t = target.get();
        if (source != null) {
            BeanUtils.copyProperties(source, t);
        }
        return t;
    }

    public static <T> T copyProperties(Object source, Supplier<T> target, Class<?> editable) {
        T t = target.get();
        if (source != null) {
            BeanUtils.copyProperties(source, t, editable);
        }
        return t;
    }

    public static <T> T copyProperties(Object source, Supplier<T> target, String... ignoreProperties) {
        T t = target.get();
        if (source != null) {
            BeanUtils.copyProperties(source, t, ignoreProperties);
        }
        return t;
    }
}
