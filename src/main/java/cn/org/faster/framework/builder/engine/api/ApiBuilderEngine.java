package cn.org.faster.framework.builder.engine.api;

import cn.org.faster.framework.builder.engine.JavaBuilderEngine;
import cn.org.faster.framework.builder.model.ColumnModel;
import cn.org.faster.framework.builder.model.TableColumnModel;
import cn.org.faster.framework.builder.utils.BuilderUtils;
import cn.org.faster.framework.builder.utils.FreemarkerUtils;
import cn.org.faster.framework.core.utils.Utils;
import freemarker.template.Template;
import org.springframework.util.StringUtils;

import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ApiBuilderEngine extends JavaBuilderEngine {
    private String baseModulePath;
    private String baseModulePackage;
    private static final String API_TEMPLATE_DIR = "api";
    private Template entityTemp = FreemarkerUtils.cfg.getTemplate(API_TEMPLATE_DIR + "/entity.ftl");
    private Template modelTemp = FreemarkerUtils.cfg.getTemplate(API_TEMPLATE_DIR + "/model.ftl");
    private Template serviceTemp = FreemarkerUtils.cfg.getTemplate(API_TEMPLATE_DIR + "/service.ftl");
    private Template controllerTemp = FreemarkerUtils.cfg.getTemplate(API_TEMPLATE_DIR + "/controller.ftl");
    private Template propertyTemp = FreemarkerUtils.cfg.getTemplate(API_TEMPLATE_DIR + "/application.yml.ftl");
    private Template buildGradleTemp = FreemarkerUtils.cfg.getTemplate(API_TEMPLATE_DIR + "/build.gradle.ftl");
    private Template baseTestTemp = FreemarkerUtils.cfg.getTemplate(API_TEMPLATE_DIR + "/baseTest.ftl");

    public ApiBuilderEngine() throws IOException {
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
        baseModulePath = JAVA_PATH + super.builderParam.getBasePath() + "/modules/v1/";
        baseModulePackage = super.builderParam.getBasePackagePath() + ".modules.v1";
        //创建压缩文件
        File  zipFile= File.createTempFile(builderParam.getProjectName(), ".zip");
        //创建模板
        List<TableColumnModel> columnModelList = builderParam.getTableColumnList();
        try (ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(zipFile))) {
            processProject(zipOutputStream);
            for (TableColumnModel tableColumnModel : columnModelList) {
                super.processMapper(tableColumnModel, zipOutputStream);
                this.processEntity(entityTemp, tableColumnModel, zipOutputStream);
                this.processModel(modelTemp, tableColumnModel, zipOutputStream);
                this.processService(serviceTemp, tableColumnModel, zipOutputStream);
                this.processController(controllerTemp, tableColumnModel, zipOutputStream);
            }
        }
        return Utils.inputStreamToByteArray(new FileInputStream(zipFile));
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
        processBaseTest(zipOutputStream);
    }

    /**
     * 生成BaseTest
     *
     * @param zipOutputStream 压缩流
     * @throws IOException io异常
     */
    private void processBaseTest(ZipOutputStream zipOutputStream) throws IOException {
        String zipFileName = TEST_JAVA_PATH + builderParam.getBasePath() + "/BaseTest.java";
        //包名
        String packageStr = "package " + builderParam.getBasePackagePath() + ";\n";
        String importStr = "import org.junit.Before;\n" +
                "import org.junit.runner.RunWith;\n" +
                "import org.springframework.beans.factory.annotation.Autowired;\n" +
                "import org.springframework.boot.test.context.SpringBootTest;\n" +
                "import org.springframework.http.HttpHeaders;\n" +
                "import org.springframework.http.MediaType;\n" +
                "import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;\n" +
                "import org.springframework.test.web.servlet.MockMvc;\n" +
                "import org.springframework.test.web.servlet.ResultActions;\n" +
                "import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;\n" +
                "import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder;\n" +
                "import org.springframework.web.context.WebApplicationContext;\n" +
                "import java.nio.charset.StandardCharsets;\n" +
                "import java.util.function.Supplier;\n" +
                "import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;\n" +
                "import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;\n";
        ;
        Map<String, Object> map = Utils.beanToMap(builderParam);
        map.put("package", packageStr);
        map.put("import", importStr);
        zipOutputStream.putNextEntry(new ZipEntry(zipFileName));
        zipOutputStream.write(FreemarkerUtils.processIntoStream(baseTestTemp, map));
        zipOutputStream.closeEntry();

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
     * 处理model
     *
     * @param template         模板
     * @param tableColumnModel 模型
     * @param zipOutputStream  压缩流
     * @throws IOException IO异常
     */
    private void processModel(Template template, TableColumnModel tableColumnModel, ZipOutputStream zipOutputStream) throws IOException {
        //文件名称
        String zipFileName = baseModulePath + tableColumnModel.getBusinessEnName() + "/model/" + tableColumnModel.getBusinessEnNameUpFirst() + "Model.java";
        //包名
        String packageStr = "package " + baseModulePackage + "." + tableColumnModel.getBusinessEnName() + ".model;\n";
        StringBuffer importStr = new StringBuffer();
        importStr
                .append("import lombok.Data;\n")
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
                .append("import com.baomidou.mybatisplus.core.metadata.IPage;\n")
                .append("import lombok.AllArgsConstructor;\n")
                .append("import org.springframework.util.StringUtils;\n")
                .append("import org.springframework.http.HttpStatus;\n")
                .append("import org.springframework.http.ResponseEntity;\n")
                .append("import org.springframework.stereotype.Service;\n")
                .append("import org.springframework.transaction.annotation.Transactional;\n")
                .append("import org.springframework.beans.BeanUtils;\n")
                .append("import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;\n")
                .append("import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;\n")
                .append("import ").append(baseModulePackage).append(".").append(tableColumnModel.getBusinessEnName()).append(".mapper.").append(tableColumnModel.getBusinessEnNameUpFirst()).append("Mapper;\n")
                .append("import ").append(baseModulePackage).append(".").append(tableColumnModel.getBusinessEnName()).append(".model.").append(tableColumnModel.getBusinessEnNameUpFirst()).append("Model;\n")
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
                .append("import ").append(baseModulePackage).append(".").append(tableColumnModel.getBusinessEnName()).append(".service.").append(tableColumnModel.getBusinessEnNameUpFirst()).append("Service;\n")
                .append("import ").append(baseModulePackage).append(".").append(tableColumnModel.getBusinessEnName()).append(".model.").append(tableColumnModel.getBusinessEnNameUpFirst()).append("Model;\n")
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
        importStr.append("import cn.org.faster.framework.core.entity.BaseEntity;\n")
                .append("import lombok.Data;\n")
                .append("import com.baomidou.mybatisplus.annotation.TableName;\n")
                .append("import lombok.EqualsAndHashCode;\n")
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
