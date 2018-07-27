package com.github.faster.framework.builder.utils;

import org.springframework.util.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BuilderUtils {
    private static final String TABLE_PREFIX = "tb_";

    /**
     * @param str 待处理
     * @return 首字母大写
     */
    public static String firstCharToUpperCase(String str) {
        char firstChar = str.charAt(0);
        if (firstChar >= 'a' && firstChar <= 'z') {
            char[] arr = str.toCharArray();
            arr[0] -= ('a' - 'A');
            return new String(arr);
        }
        return str;
    }

    /**
     * @param str 待处理
     * @return 转为驼峰
     */
    public static String hump(String str) {
        char[] strArray = str.toCharArray();
        for (int i = 0; i < strArray.length - 1; i++) {
            char curChar = strArray[i];
            char nextChar = strArray[i + 1];
            if (curChar == '_' && nextChar >= 'a' && nextChar <= 'z') {
                strArray[i + 1] -= ('a' - 'A');
            }
        }
        return new String(strArray).replace("_", "");
    }

    /**
     * @param str 待处理
     * @return 移除前缀，变驼峰
     */
    public static String delPrefixAndHump(String str) {
        int prefixIndex = str.indexOf(TABLE_PREFIX);
        if (prefixIndex > -1) {
            str = str.substring(prefixIndex + TABLE_PREFIX.length());
        }
        return hump(str);
    }

    /**
     * @param str 待处理
     * @return 过滤业务名称，移除无用注释，如：‘表‘
     */
    public static String filterBusinessCnName(String str) {
        int index = str.lastIndexOf("表");
        if (index == str.length() - 1) {
            return str.substring(0, index);
        }
        return str;
    }

    /**
     * @param basePackagePath package
     * @return package格式转换为path
     */
    public static String packagePathToPath(String basePackagePath) {
        if (StringUtils.isEmpty(basePackagePath)) {
            return null;
        }
        return basePackagePath.replace(".", "/");
    }
}
