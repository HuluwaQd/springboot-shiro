package com.example.springbootshiro.utils;

/**
 * @author wangchao
 * @version 1.0
 * @date 2020/09/08
 */
public class StringPatternUtils {

    private StringPatternUtils(){}

    /**
     * 特殊字符限20位
     */
    public static final String SPECIE_WORD_20 ="^[\\u4e00-\\u9fa50-9A-Za-z]{1,20}$";
    /**
     * 特殊字符
     */
    public static final String SPECIE_WORD = "^[a-z0-9A-Z\\u4e00-\\u9fa5]+$";
    /**
     * 手机号
     */
    public static final String PHONE_REGEX = "^1(3|4|5|6|7|8|9)\\d{9}$";
    /**
     * 座机号
     */
    public static final String TELEPHONE_REGEX = "^[0]\\d{2,3}[0-9]{7,8}$";
    /**
     * 身份证
     */
    public static final String CARD = "^$|(^[1-9]\\d{5}(18|19|20)\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$)|(^[1-9]\\d{5}\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}$)";

    public static final String NUMBER = "^[0-9]*$";
    /**
     * 非零正整数
     */
    public static final String NZNUMBER = "^+?[1-9][0-9]*$";
}
