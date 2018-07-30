package com.github.faster.framework.builder.engine.api;

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

public class ApiBuilderEngine extends JavaBuilderEngine {
    private static final String API_TEMPLATE_DIR = "api";
    private Template entityTemp = FreemarkerUtils.cfg.getTemplate(API_TEMPLATE_DIR + "/entity.ftl");
    private Template modelTemp = FreemarkerUtils.cfg.getTemplate(API_TEMPLATE_DIR + "/model.ftl");
    private Template mapperTemp = FreemarkerUtils.cfg.getTemplate(API_TEMPLATE_DIR + "/mapper.ftl");
    private Template serviceTemp = FreemarkerUtils.cfg.getTemplate(API_TEMPLATE_DIR + "/service.ftl");
    private Template controllerTemp = FreemarkerUtils.cfg.getTemplate(API_TEMPLATE_DIR + "/controller.ftl");
    private Template propertyTemp = FreemarkerUtils.cfg.getTemplate(API_TEMPLATE_DIR + "/application.yml.ftl");
    private Template applicationTemp = FreemarkerUtils.cfg.getTemplate(API_TEMPLATE_DIR + "/application.ftl");
    private Template gitIgnoreTemp = FreemarkerUtils.cfg.getTemplate(API_TEMPLATE_DIR + "/gitIgnore.ftl");
    private Template buildGradleTemp = FreemarkerUtils.cfg.getTemplate(API_TEMPLATE_DIR + "/build.gradle.ftl");
    private Template settingsGradleTemp = FreemarkerUtils.cfg.getTemplate(API_TEMPLATE_DIR + "/settings.gradle.ftl");
    private Template baseTestTemp = FreemarkerUtils.cfg.getTemplate(API_TEMPLATE_DIR + "/baseTest.ftl");

    public ApiBuilderEngine() throws IOException {
    }

    @Override
    public byte[] start() throws IOException {
        //创建压缩文件
//        File  zipFile= File.createTempFile("builderParam.getProjectName()", "zip");
        File zipFile = new File("/Users/zhangbowen/Documents", builderParam.getProjectName() + ".zip");
        //创建模板
        List<TableColumnModel> columnModelList = builderParam.getTableColumnList();
        try (ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(zipFile))) {
            processProject(zipOutputStream);
            for (TableColumnModel tableColumnModel : columnModelList) {
                super.processEntity(entityTemp, tableColumnModel, zipOutputStream);
                this.processModel(modelTemp, tableColumnModel, zipOutputStream);
                super.processMapper(mapperTemp, tableColumnModel, zipOutputStream);
                super.processService(serviceTemp, tableColumnModel, zipOutputStream);
                super.processController(controllerTemp, tableColumnModel, zipOutputStream);
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
        processYml(zipOutputStream);
        processBuildGradle(zipOutputStream);
        processSettingsGradle(zipOutputStream);
        processGitIgnore(zipOutputStream);
        processApplication(zipOutputStream);
        processBaseTest(zipOutputStream);
    }

    /**
     * 生成BaseTest
     *
     * @param zipOutputStream 压缩流
     * @throws IOException io异常
     */
    private void processBaseTest(ZipOutputStream zipOutputStream) throws IOException {
        //todo
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
     * 生成settings.gradle
     *
     * @param zipOutputStream 压缩流
     * @throws IOException io异常
     */
    private void processSettingsGradle(ZipOutputStream zipOutputStream) throws IOException {
        String zipFileName = "settings.gradle";
        zipOutputStream.putNextEntry(new ZipEntry(zipFileName));
        zipOutputStream.write(FreemarkerUtils.processIntoStream(settingsGradleTemp, builderParam));
        zipOutputStream.closeEntry();
    }

    /**
     * 生成gitIgnore
     *
     * @param zipOutputStream 压缩流
     * @throws IOException io异常
     */
    private void processGitIgnore(ZipOutputStream zipOutputStream) throws IOException {
        String zipFileName = ".gitignore";
        zipOutputStream.putNextEntry(new ZipEntry(zipFileName));
        zipOutputStream.write(FreemarkerUtils.processIntoStream(gitIgnoreTemp, builderParam));
        zipOutputStream.closeEntry();
    }

    /**
     * 生成application
     *
     * @param zipOutputStream 压缩流
     * @throws IOException io异常
     */
    private void processApplication(ZipOutputStream zipOutputStream) throws IOException {
        String zipFileName = JAVA_PATH + builderParam.getBasePath() + "/Application.java";
        //包名
        String packageStr = "package " + builderParam.getBasePackagePath() + ";\n";
        StringBuffer importStr = new StringBuffer();
        importStr
                .append("import org.springframework.boot.SpringApplication;\n")
                .append("import org.springframework.boot.autoconfigure.SpringBootApplication;\n")
        ;
        Map<String, Object> map = Utils.beanToMap(builderParam);
        map.put("package", packageStr);
        map.put("import", importStr);
        zipOutputStream.putNextEntry(new ZipEntry(zipFileName));
        zipOutputStream.write(FreemarkerUtils.processIntoStream(applicationTemp, map));
        zipOutputStream.closeEntry();
    }

    /**
     * 处理model
     *
     * @param template         模板
     * @param tableColumnModel 模型
     * @param zipOutputStream  压缩流
     * @throws IOException IO异常
     */
    protected void processModel(Template template, TableColumnModel tableColumnModel, ZipOutputStream zipOutputStream) throws IOException {
        //文件名称
        String zipFileName = baseModulePath + tableColumnModel.getBusinessEnName() + "/model/" + tableColumnModel.getBusinessEnNameUpFirst() + "Model.java";
        //包名
        String packageStr = "package " + baseModulePackage + "." + tableColumnModel.getBusinessEnName() + ".model;\n";
        StringBuffer importStr = new StringBuffer();
        importStr
                .append("import lombok.Data;\n")
                .append("import lombok.EqualsAndHashCode;\n")
                .append("import javax.persistence.Table;\n")
                .append("import javax.validation.constraints.NotBlank;\n")
                .append("import javax.validation.constraints.NotNull;\n")
        ;
        List<ColumnModel> columnList = tableColumnModel.getColumnList().stream()
                .filter(item -> BuilderUtils.baseNotContainsProperty(item.getColumnNameHump()))
                .peek(item -> {
                    if (!StringUtils.isEmpty(item.getJavaImportType()) && importStr.indexOf(item.getJavaImportType()) == -1) {
                        importStr.append(item.getJavaImportType()).append("\n");
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
