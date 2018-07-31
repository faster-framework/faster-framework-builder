package com.github.faster.framework.builder.engine.adminApi;

import com.github.faster.framework.builder.engine.JavaBuilderEngine;
import com.github.faster.framework.builder.model.ColumnModel;
import com.github.faster.framework.builder.model.TableColumnModel;
import com.github.faster.framework.builder.utils.BuilderUtils;
import com.github.faster.framework.builder.utils.FreemarkerUtils;
import com.github.faster.framework.core.utils.Utils;
import freemarker.template.Template;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class AdminApiBuilderEngine extends JavaBuilderEngine {
    private String baseModulePath;
    private String baseModulePackage;
    private static final String ADMIN_TEMPLATE_DIR = "adminApi";
    private Template entityTemp = FreemarkerUtils.cfg.getTemplate(ADMIN_TEMPLATE_DIR + "/entity.ftl");
    private Template serviceTemp = FreemarkerUtils.cfg.getTemplate(ADMIN_TEMPLATE_DIR + "/service.ftl");
    private Template controllerTemp = FreemarkerUtils.cfg.getTemplate(ADMIN_TEMPLATE_DIR + "/controller.ftl");
    private Template propertyTemp = FreemarkerUtils.cfg.getTemplate(ADMIN_TEMPLATE_DIR + "/application.yml.ftl");
    private Template buildGradleTemp = FreemarkerUtils.cfg.getTemplate(ADMIN_TEMPLATE_DIR + "/build.gradle.ftl");

    public AdminApiBuilderEngine() throws IOException {
    }

    @Override
    protected String getBaseModulePath() {
        return this.baseModulePath;
    }

    @Override
    protected String getBaseModulePackage() {
        return this.baseModulePackage;
    }

    @Override
    public byte[] start() throws IOException {
        baseModulePath = JAVA_PATH + super.builderParam.getBasePath() + "/modules/";
        baseModulePackage = super.builderParam.getBasePackagePath() + ".modules";
        //创建压缩文件
//        File  zipFile= File.createTempFile("builderParam.getProjectName()", "zip");
        File zipFile = new File("/Users/zhangbowen/Documents", builderParam.getProjectName() + ".zip");
        //创建模板
        List<TableColumnModel> columnModelList = builderParam.getTableColumnList();
        try (ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(zipFile))) {
            processProject(zipOutputStream);
            for (TableColumnModel tableColumnModel : columnModelList) {
                super.processMapper(tableColumnModel, zipOutputStream);
                this.processEntity(entityTemp, tableColumnModel, zipOutputStream);
                this.processService(serviceTemp, tableColumnModel, zipOutputStream);
                this.processController(controllerTemp, tableColumnModel, zipOutputStream);
            }
        }

        return new byte[0];
    }

    /**
     * 生成项目主框架
     *
     * @param zipOutputStream 压缩流
     * @throws IOException io异常
     */
    private void processProject(ZipOutputStream zipOutputStream) throws IOException {
        super.processSettingsGradle(zipOutputStream);
        super.processGitIgnore(zipOutputStream);
        super.processApplication(zipOutputStream);
        processYml(zipOutputStream);
        processBuildGradle(zipOutputStream);
    }


    /**
     * 生成yml文件
     *
     * @param zipOutputStream 压缩流
     * @throws IOException io异常
     */
    private void processYml(ZipOutputStream zipOutputStream) throws IOException {
        String zipFileName = RESOURCES_PATH + "application.yml";
        zipOutputStream.putNextEntry(new ZipEntry(zipFileName));
        zipOutputStream.write(FreemarkerUtils.processIntoStream(propertyTemp, builderParam));
        zipOutputStream.closeEntry();
    }

    /**
     * 生成build.gradle
     *
     * @param zipOutputStream 压缩流
     * @throws IOException io异常
     */
    private void processBuildGradle(ZipOutputStream zipOutputStream) throws IOException {
        String zipFileName = "build.gradle";
        zipOutputStream.putNextEntry(new ZipEntry(zipFileName));
        zipOutputStream.write(FreemarkerUtils.processIntoStream(buildGradleTemp, builderParam));
        zipOutputStream.closeEntry();
    }

    /**
     * 处理service
     *
     * @param template         模板
     * @param tableColumnModel 模型
     * @param zipOutputStream  压缩文件
     * @throws IOException IO异常
     */
    private void processService(Template template, TableColumnModel tableColumnModel, ZipOutputStream zipOutputStream) throws IOException {
        //文件名称
        String zipFileName = baseModulePath + tableColumnModel.getBusinessEnName() + "/service/" + tableColumnModel.getBusinessEnNameUpFirst() + "Service.java";
        //包名
        String packageStr = "package " + baseModulePackage + "." + tableColumnModel.getBusinessEnName() + ".service;\n";
        StringBuffer importStr = new StringBuffer()
                .append("import com.github.pagehelper.PageInfo;\n")
                .append("import lombok.AllArgsConstructor;\n")
                .append("import org.springframework.util.StringUtils;\n")
                .append("import org.springframework.http.HttpStatus;\n")
                .append("import org.springframework.http.ResponseEntity;\n")
                .append("import org.springframework.stereotype.Service;\n")
                .append("import org.springframework.transaction.annotation.Transactional;\n")
                .append("import tk.mybatis.mapper.entity.Example;\n")
                .append("import tk.mybatis.mapper.weekend.WeekendSqls;\n")
                .append("import ").append(baseModulePackage).append(".").append(tableColumnModel.getBusinessEnName()).append(".mapper.").append(tableColumnModel.getBusinessEnNameUpFirst()).append("Mapper;\n")
                .append("import ").append(baseModulePackage).append(".").append(tableColumnModel.getBusinessEnName()).append(".entity.").append(tableColumnModel.getBusinessEnNameUpFirst()).append(";\n");
        List<ColumnModel> columnList = tableColumnModel.getColumnList().stream()
                .filter(item -> BuilderUtils.baseNotContainsProperty(item.getColumnNameHump())).collect(Collectors.toList());
        Map<String, Object> map = Utils.beanToMap(tableColumnModel);
        map.put("package", packageStr);
        map.put("import", importStr);
        map.put("columnList", columnList);
        zipOutputStream.putNextEntry(new ZipEntry(zipFileName));
        zipOutputStream.write(FreemarkerUtils.processIntoStream(template, map));
        zipOutputStream.closeEntry();
    }

    /**
     * 处理controller
     *
     * @param template         模板
     * @param tableColumnModel 模型
     * @param zipOutputStream  压缩文件
     * @throws IOException IO异常
     */
    private void processController(Template template, TableColumnModel tableColumnModel, ZipOutputStream zipOutputStream) throws IOException {
        //文件名称
        String zipFileName = baseModulePath + tableColumnModel.getBusinessEnName() + "/controller/" + tableColumnModel.getBusinessEnNameUpFirst() + "Controller.java";
        //包名
        String packageStr = "package " + baseModulePackage + "." + tableColumnModel.getBusinessEnName() + ".controller;\n";
        StringBuffer importStr = new StringBuffer()
                .append("import lombok.AllArgsConstructor;\n")
                .append("import org.springframework.http.ResponseEntity;\n")
                .append("import org.springframework.web.bind.annotation.*;\n")
                .append("import org.springframework.validation.annotation.Validated;\n")
                .append("import org.apache.shiro.authz.annotation.RequiresPermissions;\n")
                .append("import ").append(baseModulePackage).append(".").append(tableColumnModel.getBusinessEnName()).append(".service.").append(tableColumnModel.getBusinessEnNameUpFirst()).append("Service;\n")
                .append("import ").append(baseModulePackage).append(".").append(tableColumnModel.getBusinessEnName()).append(".entity.").append(tableColumnModel.getBusinessEnNameUpFirst()).append(";\n");
        Map<String, Object> map = Utils.beanToMap(tableColumnModel);
        map.put("package", packageStr);
        map.put("import", importStr);
        zipOutputStream.putNextEntry(new ZipEntry(zipFileName));
        zipOutputStream.write(FreemarkerUtils.processIntoStream(template, map));
        zipOutputStream.closeEntry();
    }

    /**
     * 处理实体
     *
     * @param template         模板
     * @param tableColumnModel 模型
     * @param zipOutputStream  压缩文件
     * @throws IOException IO异常
     */
    private void processEntity(Template template, TableColumnModel tableColumnModel, ZipOutputStream zipOutputStream) throws IOException {
        //文件名称
        String zipFileName = baseModulePath + tableColumnModel.getBusinessEnName() + "/entity/" + tableColumnModel.getBusinessEnNameUpFirst() + ".java";
        //包名
        String packageStr = "package " + baseModulePackage + "." + tableColumnModel.getBusinessEnName() + ".entity;\n";
        StringBuffer importStr = new StringBuffer();
        importStr.append("import com.github.faster.framework.core.entity.BaseEntity;\n")
                .append("import lombok.Data;\n")
                .append("import lombok.EqualsAndHashCode;\n")
                .append("import javax.persistence.Table;\n")
        ;
        List<ColumnModel> columnList = tableColumnModel.getColumnList().stream()
                .filter(item -> BuilderUtils.baseNotContainsProperty(item.getColumnNameHump()))
                .peek(item -> {
                    if (!StringUtils.isEmpty(item.getJavaImportType()) && importStr.indexOf(item.getJavaImportType()) == -1) {
                        importStr.append(item.getJavaImportType()).append("\n");
                    }
                    if (item.getIsNullable().equals("NO")) {
                        if (item.getJavaType().equals("String") && importStr.indexOf("import javax.validation.constraints.NotBlank;") == -1) {
                            importStr.append("import javax.validation.constraints.NotBlank;\n");
                        } else if (!item.getJavaType().equals("String") && importStr.indexOf("import javax.validation.constraints.NotNull;") == -1) {
                            importStr.append("import javax.validation.constraints.NotNull;\n");
                        }
                    }
                }).collect(Collectors.toList());
        Map<String, Object> map = Utils.beanToMap(tableColumnModel);
        map.put("columnList", columnList);
        map.put("package", packageStr);
        map.put("import", importStr);
        zipOutputStream.putNextEntry(new ZipEntry(zipFileName));
        zipOutputStream.write(FreemarkerUtils.processIntoStream(template, map));
        zipOutputStream.closeEntry();
    }

}
