package cn.org.faster.framework.builder.common.utils;

import cn.org.faster.framework.builder.common.model.ColumnModel;
import cn.org.faster.framework.builder.common.model.DependencyModel;
import cn.org.faster.framework.builder.common.model.TableColumnModel;
import cn.org.faster.framework.builder.modules.strategy.db.PermissionModel;
import freemarker.template.Template;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 回归测试：29 个模板必须全部能加载并渲染成功。
 *
 * <p>该类用于兜住"模板名写错、变量漏传、渲染异常被吞掉"这类问题。</p>
 */
@DisplayName("FreeMarker 模板渲染")
class FreemarkerTemplateRenderTest {

    @Test
    @DisplayName("全部模板可渲染且无未解析占位符")
    void renderAllTemplates() throws Exception {
        Resource[] resources = new PathMatchingResourcePatternResolver()
                .getResources("classpath*:/template/**/*.ftl");
        assertThat(resources).isNotEmpty();

        for (Resource resource : resources) {
            String path = resource.getURL().getPath();
            String name = path.substring(path.indexOf("/template/") + "/template/".length());
            Template template = FreemarkerUtils.cfg.getTemplate("/" + name);
            String rendered = new String(FreemarkerUtils.processIntoStream(template, model()), StandardCharsets.UTF_8);

            assertThat(rendered)
                    .as("模板 %s 渲染结果", name)
                    .isNotBlank();
            assertThat(rendered)
                    .as("模板 %s 渲染后不应残留占位符", name)
                    .doesNotContain("${");
        }
    }

    @Test
    @DisplayName("实体模板按列生成字段并过滤 BaseEntity 字段")
    void entityTemplate() throws Exception {
        Template template = FreemarkerUtils.cfg.getTemplate("java/entity.ftl");
        String rendered = new String(FreemarkerUtils.processIntoStream(template, model()), StandardCharsets.UTF_8);

        assertThat(rendered)
                .contains("@TableName(\"tb_user\")", "private String userName;")
                .doesNotContain("private LocalDateTime createDate;", "private Long id;");
    }

    private static Map<String, Object> model() {
        ColumnModel name = column("user_name", "varchar", "NO", "用户名");
        ColumnModel createDate = column("create_date", "datetime", "YES", "创建时间");

        TableColumnModel table = new TableColumnModel();
        table.setTableName("tb_user");
        table.setTableSchema("demo");
        table.setTableComment("用户表");
        table.setBusinessEnName("user");
        table.setBusinessEnNameUpFirst("User");
        table.setBusinessCnName("用户");
        table.setColumnList(List.of(name, createDate));

        PermissionModel permission = new PermissionModel();
        permission.setName("用户管理");
        permission.setCode("user:manage");
        permission.setParentId(0L);
        permission.setParentIds("[0]");
        permission.preInsert();

        DependencyModel dependency = new DependencyModel();
        dependency.setGroupId("cn.org.faster");
        dependency.setArtifactId("spring-boot-starter-web");

        Map<String, Object> map = new HashMap<>(BuilderUtils.beanToMap(table));
        map.put("package", "package com.demo.modules.user;\n");
        map.put("import", "import java.util.List;\n");
        map.put("headerToken", "header.payload.signature");
        map.put("requestType", "Add");
        map.put("permissionList", List.of(permission));
        map.put("dependencies", List.of(dependency));
        map.put("columnList", List.of(name));
        map.put("tableColumnList", List.of(table));
        map.put("projectName", "demo");
        map.put("basePackagePath", "com.demo");
        map.put("basePath", "com/demo");
        map.put("dependencyVersion", "1.4.2.RELEASE");
        map.put("dependencyUrl", "https://example.com/skeleton.zip");
        map.put("dbHost", "127.0.0.1");
        map.put("dbPort", "3306");
        map.put("dbName", "demo");
        map.put("dbUsername", "root");
        map.put("dbPwd", "secret");
        return map;
    }

    private static ColumnModel column(String name, String dataType, String nullable, String comment) {
        ColumnModel column = new ColumnModel();
        column.setColumnName(name);
        column.setDataType(dataType);
        column.setOrdinalPosition(1);
        column.setColumnComment(comment);
        column.setIsNullable(nullable);
        column.setColumnNameHump(BuilderUtils.hump(name));
        column.setColumnNameHumpUpFirst(BuilderUtils.firstCharToUpperCase(column.getColumnNameHump()));
        column.setJavaType(BuilderUtils.convertToJavaType(dataType));
        column.setJavaImportType(BuilderUtils.javaImportType(column.getJavaType()));
        return column;
    }
}
