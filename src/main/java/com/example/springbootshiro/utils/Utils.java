package com.example.springbootshiro.utils;



import com.fasterxml.uuid.Generators;
import org.apache.commons.lang3.SystemUtils;
import org.apache.commons.lang3.RandomUtils;

import java.util.UUID;

/**
 * @author wangzw
 */
public final class Utils {

    public static String getNoDashUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    public static String getUUID() {
        return UUID.randomUUID().toString();
    }

    public static UUID getTimeBasedUUID() {
        return Generators.timeBasedGenerator().generate();
    }

    public static long nextLong(final long startInclusive, final long endExclusive) {
        return  RandomUtils.nextLong(startInclusive,endExclusive);
    }

    public static String getHostName() {
        return SystemUtils.getHostName();
    }

    private Utils() {}

}
