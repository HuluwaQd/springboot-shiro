package com.example.springbootshiro.utils;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public final class Base64Utils {

    private Base64Utils() {
    }

    public static String encodeUrl(byte[] bytes) {
        return Base64.getUrlEncoder().encodeToString(bytes);
    }

    public static String encodeUrl(String string) {
        return Base64.getUrlEncoder().encodeToString(string.getBytes(StandardCharsets.UTF_8));
    }

    public static String encodeUrl(String string, String charset) {
        return Base64.getUrlEncoder().encodeToString(string.getBytes(Charset.forName(charset)));
    }

    public static String decodeUrlToString(String encode) {
        return new String(Base64.getUrlDecoder().decode(encode), StandardCharsets.UTF_8);
    }

    public static byte[] decodeUrl(String encode) {
        return Base64.getUrlDecoder().decode(encode);
    }

    public static String decodeUrlToString(byte[] bytes, String charset) throws UnsupportedEncodingException {
        return new String(Base64.getUrlDecoder().decode(bytes), charset);
    }

    public static byte[] encode(byte[] bytes) {
        return Base64.getEncoder().encode(bytes);
    }

    public static String encodeToString(byte[] bytes) {
        return Base64.getEncoder().encodeToString(bytes);
    }

    public static String encodeToString(String string, String charset) {
        return Base64.getEncoder().encodeToString(string.getBytes(Charset.forName(charset)));
    }

    public static String encodeToString(String string) {
        return Base64.getEncoder().encodeToString(string.getBytes(StandardCharsets.UTF_8));
    }

    public static byte[] decode(String encode) {
        return Base64.getDecoder().decode(encode);
    }

    public static byte[] decode(byte[] encode) {
        return Base64.getDecoder().decode(encode);
    }

    public static String decodeToString(byte[] encode) {
        return new String(Base64.getDecoder().decode(encode),StandardCharsets.UTF_8);
    }

    public static String decodeToString(String encode) {
        return new String(Base64.getDecoder().decode(encode), StandardCharsets.UTF_8);
    }

    public static String decodeToString(byte[] bytes, String charset) throws UnsupportedEncodingException {
        return new String(Base64.getDecoder().decode(bytes), charset);
    }
}
