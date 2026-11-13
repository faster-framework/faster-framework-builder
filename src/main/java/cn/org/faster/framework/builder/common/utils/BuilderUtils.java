package cn.org.faster.framework.builder.common.utils;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 命名转换与元数据推导工具。
 *
 * <p>与旧版的差异：</p>
 * <ul>
 *   <li>不再依赖框架 {@code BaseEntity}（改用显式字段集合），生成器因此可脱离 Faster 框架独立构建；</li>
 *   <li>不再依赖 cglib {@code BeanMap}，改用 JDK {@link Introspector}；</li>
 *   <li>不再使用 Spring 已废弃的 {@code StringUtils.isEmpty}。</li>
 * </ul>
 *
 * @author zhangbowen
 */
public final class BuilderUtils {

    private static final List<String> TABLE_PREFIX_LIST = List.of("tb_");

    /**
     * 生成实体/请求对象时应当跳过的字段：框架 {@code BaseEntity} 已提供的公共字段。
     */
    private static final Set<String> BASE_ENTITY_PROPERTIES = Set.of(
            "id", "createBy", "updateBy", "createDate", "updateDate",
            "sort", "remark", "deleted", "current", "size");

    private BuilderUtils() {
    }

    /**
     * @param str 待处理
     * @return 首字母大写
     */
    public static String firstCharToUpperCase(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
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
        if (str == null || str.isEmpty()) {
            return str;
        }
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
        if (str == null) {
            return null;
        }
        String result = str;
        for (String prefix : TABLE_PREFIX_LIST) {
            int prefixIndex = result.indexOf(prefix);
            if (prefixIndex > -1) {
                result = result.substring(prefixIndex + prefix.length());
                break;
            }
        }
        return hump(result);
    }

    /**
     * @param str 待处理
     * @return 过滤业务名称，移除无用注释，如：‘表‘
     */
    public static String filterBusinessCnName(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
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
        if (isBlank(basePackagePath)) {
            return null;
        }
        return basePackagePath.replace(".", "/");
    }

    /**
     * @param dataType 数据库类型
     * @return Java类型
     */
    public static String convertToJavaType(String dataType) {
        if (isBlank(dataType)) {
            return null;
        }
        return switch (dataType.toUpperCase()) {
            case "TINYINT", "SMALLINT", "MEDIUMINT", "INT", "INTEGER" -> "Integer";
            case "BIGINT" -> "Long";
            case "FLOAT" -> "Float";
            case "DOUBLE" -> "Double";
            case "DECIMAL" -> "BigDecimal";
            case "DATE", "TIME", "YEAR", "DATETIME", "TIMESTAMP" -> "LocalDateTime";
            default -> "String";
        };
    }

    /**
     * @param javaType java类型
     * @return 需要引入的包
     */
    public static String javaImportType(String javaType) {
        if (isBlank(javaType)) {
            return null;
        }
        return switch (javaType) {
            case "LocalDateTime" -> "import java.time.LocalDateTime;";
            case "BigDecimal" -> "import java.math.BigDecimal;";
            default -> null;
        };
    }

    /**
     * 该属性是否不属于框架 BaseEntity（即需要生成到实体/请求对象中）。
     *
     * @param property 属性名（驼峰）
     * @return true 表示需要生成
     */
    public static boolean baseNotContainsProperty(String property) {
        return property == null || !BASE_ENTITY_PROPERTIES.contains(property);
    }

    /**
     * JavaBean 转 Map，供 FreeMarker 模板使用。
     *
     * @param bean 原始对象
     * @return 属性名 → 属性值（含继承属性）
     */
    public static Map<String, Object> beanToMap(Object bean) {
        if (bean == null) {
            return Map.of();
        }
        Map<String, Object> map = new HashMap<>();
        try {
            for (PropertyDescriptor descriptor : Introspector.getBeanInfo(bean.getClass(), Object.class)
                    .getPropertyDescriptors()) {
                Method readMethod = descriptor.getReadMethod();
                if (readMethod != null && readMethod.getParameterCount() == 0) {
                    map.put(descriptor.getName(), readMethod.invoke(bean));
                }
            }
        } catch (IntrospectionException | IllegalAccessException | InvocationTargetException ex) {
            throw new IllegalStateException("无法读取 " + bean.getClass().getName() + " 的属性", ex);
        }
        return map;
    }

    /**
     * @param value 待判断
     * @return null、空串或全空白
     */
    public static boolean isBlank(String value) {
        return value == null || value.isBlank();
    }

    /**
     * @param value 待判断
     * @return 与 {@link #isBlank} 相反
     */
    public static boolean isNotBlank(String value) {
        return !isBlank(value);
    }
}
