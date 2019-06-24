package org.smart4j.framework.util;

/**
 * 转型操作工具类
 */
public final class CastUtil {

    /**
     * 转为String
     */
    public static String castString(Object obj) {
        return CastUtil.castString(obj, "");
    }

    /**
     * 转为String(提供默认值)
     */
    public static String castString(Object obj, String defaultValue) {
        return obj != null ? String.valueOf(obj) : defaultValue;
    }
    /**
     * 转为int型
     */
    public static int castInt(Object obj) {
        return castInt(obj, 0);
    }
    /**
     * 转为int型(提供默认值)
     */
    public static int castInt(Object obj, int defaultValue) {
        int intValue = defaultValue;
        if(obj != null) {
            String strValue = castString(obj);
            if(StringUtil.isNotEmpty(strValue)) {
                try {
                    defaultValue = Integer.parseInt(strValue);
                } catch (NumberFormatException e) {
                    defaultValue = defaultValue;
                }
            }
        }
        return defaultValue;
    }

    /**
     * 转为long型(提供默认值)
     */
    public static long castLong(Object obj, long defaultValue){
        long longValue = defaultValue;
        if(obj != null) {
            String strValue = castString(obj);
            if(StringUtil.isNotEmpty(strValue)) {
                try{
                    longValue = Long.parseLong(strValue);
                } catch (NumberFormatException e) {
                    longValue = defaultValue;
                }
            }
        }
        return longValue;
    }

    /**
     * 转为long型
     */
    public static long castLong(Object obj) {
        return castLong(obj, 0);
    }

    /**
     * 转为float型
     */
    public static float castFloat(Object obj) {
        return castFloat(obj, 0);
    }
    /**
     * 转为float型(提供默认值)
     */
    public static float castFloat(Object obj, float defaultValue) {
        float floatValue = defaultValue;
        if(obj != null) {
            String strValue = castString(obj);
            if(StringUtil.isNotEmpty(strValue)) {
                try {
                    defaultValue = Float.parseFloat(strValue);
                } catch (NumberFormatException e) {
                    defaultValue = defaultValue;
                }
            }
        }
        return defaultValue;
    }
    /**
     * 转为double型
     */
    public static double castDouble(Object obj) {
        return castDouble(obj, 0);
    }
    /**
     * 转为double型(提供默认值)
     */
    public static double castDouble(Object obj, double defaultValue) {
        double doubleValue = defaultValue;
        if(obj != null) {
            String strValue = castString(obj);
            if(StringUtil.isNotEmpty(strValue)) {
                try {
                    defaultValue = Double.parseDouble(strValue);
                } catch (NumberFormatException e) {
                    defaultValue = defaultValue;
                }
            }
        }
        return defaultValue;
    }

    /**
     * 转为boolean类型
     */
    public static boolean castBoolean(Object obj) {
        return CastUtil.castBoolean(obj, false);
    }

    /**
     * 转为boolean类型(提供默认值)
     */
    public static boolean castBoolean(Object obj, boolean defaultValue) {
        boolean booleanValue = defaultValue;
        if(obj != null) {
            booleanValue = Boolean.parseBoolean(castString(obj));
        }
        return booleanValue;
    }

}
