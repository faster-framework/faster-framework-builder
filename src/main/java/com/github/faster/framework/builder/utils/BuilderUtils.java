package com.github.faster.framework.builder.utils;

import com.github.faster.framework.core.entity.BaseEntity;
import org.springframework.beans.BeanUtils;
import org.springframework.util.StringUtils;

import java.beans.PropertyDescriptor;
import java.util.Arrays;
import java.util.List;

public class BuilderUtils {
    private static final List<String> TABLE_PREFIX_LIST = Arrays.asList("tb_");

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
        int prefixIndex = -1;
        for (String filter : TABLE_PREFIX_LIST) {
            prefixIndex = str.indexOf(filter);
            if (prefixIndex > -1) {
                str = str.substring(prefixIndex + filter.length());
                break;
            }
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

    /**
     * @param dataType 数据库类型
     * @return Java类型
     */
    public static String convertToJavaType(String dataType) {
        if (StringUtils.isEmpty(dataType)) {
            return null;
        }
        switch (dataType.toUpperCase()) {
            case "TINYINT":
            case "SMALLINT":
            case "MEDIUMINT":
            case "INT":
            case "INTEGER":
                return "Integer";
            case "BIGINT":
                return "Long";
            case "FLOAT":
                return "Float";
            case "DOUBLE":
                return "Double";
            case "DECIMAL":
                return "BigDecimal";
            case "DATE":
            case "TIME":
            case "YEAR":
            case "DATETIME":
            case "TIMESTAMP":
                return "LocalDateTime";
            default:
                return "String";

        }
    }

    /**
     * @param javaType java类型
     * @return 需要引入的包
     */
    public static String javaImportType(String javaType) {
        if (StringUtils.isEmpty(javaType)) {
            return null;
        }
        switch (javaType) {
            case "LocalDateTime":
                return "import java.time.LocalDateTime;";
            case "BigDecimal":
                return "import java.math.BigDecimal;";
            default:
                return null;
        }
    }

    /**
     * BaseEntity是否含有属性
     *
     * @param property 属性
     * @return true or false
     */
    public static boolean baseNotContainsProperty(String property) {
        PropertyDescriptor propertyDescriptor = BeanUtils.getPropertyDescriptor(BaseEntity.class, property);
        return propertyDescriptor == null;
    }

}
