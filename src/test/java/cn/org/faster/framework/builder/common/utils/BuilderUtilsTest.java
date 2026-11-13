package cn.org.faster.framework.builder.common.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("BuilderUtils 命名与类型推导")
class BuilderUtilsTest {

    @Test
    @DisplayName("下划线转驼峰")
    void hump() {
        assertThat(BuilderUtils.hump("user_name")).isEqualTo("userName");
        assertThat(BuilderUtils.hump("id")).isEqualTo("id");
    }

    @Test
    @DisplayName("首字母大写")
    void firstCharToUpperCase() {
        assertThat(BuilderUtils.firstCharToUpperCase("userName")).isEqualTo("UserName");
        assertThat(BuilderUtils.firstCharToUpperCase("UserName")).isEqualTo("UserName");
        assertThat(BuilderUtils.firstCharToUpperCase("")).isEmpty();
    }

    @Test
    @DisplayName("去掉 tb_ 前缀并转驼峰")
    void delPrefixAndHump() {
        assertThat(BuilderUtils.delPrefixAndHump("tb_user_info")).isEqualTo("userInfo");
        assertThat(BuilderUtils.delPrefixAndHump("order")).isEqualTo("order");
    }

    @Test
    @DisplayName("去掉表注释末尾的“表”")
    void filterBusinessCnName() {
        assertThat(BuilderUtils.filterBusinessCnName("用户表")).isEqualTo("用户");
        assertThat(BuilderUtils.filterBusinessCnName("用户信息")).isEqualTo("用户信息");
    }

    @Test
    @DisplayName("包路径转目录路径")
    void packagePathToPath() {
        assertThat(BuilderUtils.packagePathToPath("com.example.demo")).isEqualTo("com/example/demo");
        assertThat(BuilderUtils.packagePathToPath("  ")).isNull();
        assertThat(BuilderUtils.packagePathToPath(null)).isNull();
    }

    @Test
    @DisplayName("数据库类型到 Java 类型")
    void convertToJavaType() {
        assertThat(BuilderUtils.convertToJavaType("int")).isEqualTo("Integer");
        assertThat(BuilderUtils.convertToJavaType("BIGINT")).isEqualTo("Long");
        assertThat(BuilderUtils.convertToJavaType("decimal")).isEqualTo("BigDecimal");
        assertThat(BuilderUtils.convertToJavaType("datetime")).isEqualTo("LocalDateTime");
        assertThat(BuilderUtils.convertToJavaType("varchar")).isEqualTo("String");
        assertThat(BuilderUtils.convertToJavaType(" ")).isNull();
    }

    @Test
    @DisplayName("Java 类型到 import 语句")
    void javaImportType() {
        assertThat(BuilderUtils.javaImportType("LocalDateTime")).isEqualTo("import java.time.LocalDateTime;");
        assertThat(BuilderUtils.javaImportType("BigDecimal")).isEqualTo("import java.math.BigDecimal;");
        assertThat(BuilderUtils.javaImportType("String")).isNull();
    }

    @Test
    @DisplayName("BaseEntity 字段不重复生成")
    void baseNotContainsProperty() {
        assertThat(BuilderUtils.baseNotContainsProperty("id")).isFalse();
        assertThat(BuilderUtils.baseNotContainsProperty("createDate")).isFalse();
        assertThat(BuilderUtils.baseNotContainsProperty("userName")).isTrue();
    }

    @Test
    @DisplayName("Bean 转 Map 包含继承属性")
    void beanToMap() {
        SampleBean bean = new SampleBean();
        bean.setName("faster");
        bean.setCount(3);

        Map<String, Object> map = BuilderUtils.beanToMap(bean);

        assertThat(map).containsEntry("name", "faster").containsEntry("count", 3);
        assertThat(BuilderUtils.beanToMap(null)).isEmpty();
    }

    @Test
    @DisplayName("空白判断")
    void blank() {
        assertThat(BuilderUtils.isBlank(null)).isTrue();
        assertThat(BuilderUtils.isBlank("  ")).isTrue();
        assertThat(BuilderUtils.isNotBlank("x")).isTrue();
    }

    static class SampleBean {
        private String name;
        private Integer count;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getCount() {
            return count;
        }

        public void setCount(Integer count) {
            this.count = count;
        }
    }
}
