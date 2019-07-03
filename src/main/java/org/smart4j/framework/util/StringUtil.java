package org.smart4j.framework.util;

import org.apache.commons.lang3.StringUtils;

/**
 * 字符串工具类
 */
public final class StringUtil {

    /**
     * 字符串分隔符
     */
    public static final String SEPARATOR = String.valueOf((char) 29);
    /**
     * 判断字符串是否为空
     */
    public static boolean isEmpty(String str) {
        if(str != null) {
            str = str.trim();
        }
        return StringUtils.isEmpty(str);
    }
    /**
     * 判断字符串非空
     */
    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    /**
     * 拆分字符串
     */
    public static String[] splitString(String str, String rex) {
        String[] strings = null;
        if(isNotEmpty(str)) {
            strings = str.split(rex);
        }
        return strings;
    }
}
