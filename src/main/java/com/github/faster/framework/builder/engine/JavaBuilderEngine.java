package com.github.faster.framework.builder.engine;

import com.github.faster.framework.builder.model.ColumnModel;
import com.github.faster.framework.builder.model.TableColumnModel;
import com.github.faster.framework.builder.model.request.BuilderRequest;
import com.github.faster.framework.builder.utils.BuilderUtils;
import com.github.faster.framework.builder.utils.FreemarkerUtils;
import com.github.faster.framework.core.utils.Utils;
import freemarker.template.Template;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


public abstract class JavaBuilderEngine extends BuilderEngine {
    protected String baseModulePath;
    protected String baseModulePackage;
    protected final String JAVA_PATH = "/src/main/java/";
    protected final String TEST_JAVA_PATH = "/src/test/java/";
    protected final String RESOURCES_PATH = "/src/main/resources/";

    /**
     * 初始化
     *
     * @param builderRequest  请求参数
     * @param tableColumnList 表、列
     */
    protected void init(BuilderRequest builderRequest, List<TableColumnModel> tableColumnList) {
        super.init(builderRequest, tableColumnList);
        baseModulePath = JAVA_PATH + super.builderParam.getBasePath() + "/modules/v1/";
        baseModulePackage = super.builderParam.getBasePackagePath() + ".modules.v1";
    }

    /**
     * 处理实体
     *
     * @param template         模板
     * @param tableColumnModel 模型
     * @param zipOutputStream  压缩文件
     * @throws IOException IO异常
     */
    protected void processEntity(Template template, TableColumnModel tableColumnModel, ZipOutputStream zipOutputStream) throws IOException {
        //文件名称
        String zipFileName = baseModulePath + tableColumnModel.getBusinessEnName() + "/entity/" + tableColumnModel.getBusinessEnNameUpFirst() + ".java";
        //包名
        String packageStr = "package " + baseModulePackage + "." + tableColumnModel.getBusinessEnName() + ".entity;\n";
        StringBuffer importStr = new StringBuffer();
        importStr.append("import com.github.faster.framework.core.entity.BaseEntity;\n")
                .append("import lombok.Data;\n")
                .append("import lombok.EqualsAndHashCode;\n")
                .append("import javax.persistence.Table;\n");
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

    /**
     * 处理mapper
     *
     * @param template         模板
     * @param tableColumnModel 模型
     * @param zipOutputStream  压缩文件
     * @throws IOException IO异常
     */
    protected void processMapper(Template template, TableColumnModel tableColumnModel, ZipOutputStream zipOutputStream) throws IOException {
        //文件名称
        String zipFileName = baseModulePath + tableColumnModel.getBusinessEnName() + "/mapper/" + tableColumnModel.getBusinessEnNameUpFirst() + "Mapper.java";
        //包名
        String packageStr = "package " + baseModulePackage + "." + tableColumnModel.getBusinessEnName() + ".mapper;\n";
        StringBuffer importStr = new StringBuffer();
        importStr.append("import com.github.faster.framework.core.mybatis.mapper.BaseMapper;\n")
                .append("import ").append(baseModulePackage).append(".").append(tableColumnModel.getBusinessEnName()).append(".entity.").append(tableColumnModel.getBusinessEnNameUpFirst()).append(";\n");
        Map<String, Object> map = Utils.beanToMap(tableColumnModel);
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
    protected void processService(Template template, TableColumnModel tableColumnModel, ZipOutputStream zipOutputStream) throws IOException {
        //文件名称
        String zipFileName = baseModulePath + tableColumnModel.getBusinessEnName() + "/service/" + tableColumnModel.getBusinessEnNameUpFirst() + "Service.java";
        //包名
        String packageStr = "package " + baseModulePackage + "." + tableColumnModel.getBusinessEnName() + ".service;\n";
        StringBuffer importStr = new StringBuffer()
                .append("import com.github.pagehelper.PageInfo;\n")
                .append("import lombok.AllArgsConstructor;\n")
                .append("import org.springframework.http.HttpStatus;\n")
                .append("import org.springframework.http.ResponseEntity;\n")
                .append("import org.springframework.stereotype.Service;\n")
                .append("import org.springframework.transaction.annotation.Transactional;\n")
                .append("import org.springframework.beans.BeanUtils;\n")
                .append("import tk.mybatis.mapper.entity.Example;\n")
                .append("import tk.mybatis.mapper.weekend.WeekendSqls;\n")
                .append("import com.github.faster.framework.core.exception.model.ErrorResponseEntity;\n")
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
    protected void processController(Template template, TableColumnModel tableColumnModel, ZipOutputStream zipOutputStream) throws IOException {
        //文件名称
        String zipFileName = baseModulePath + tableColumnModel.getBusinessEnName() + "/controller/" + tableColumnModel.getBusinessEnNameUpFirst() + "Controller.java";
        //包名
        String packageStr = "package " + baseModulePackage + "." + tableColumnModel.getBusinessEnName() + ".controller;\n";
        StringBuffer importStr = new StringBuffer()
                .append("import lombok.AllArgsConstructor;\n")
                .append("import org.springframework.http.ResponseEntity;\n")
                .append("import org.springframework.web.bind.annotation.RestController;\n")
                .append("import org.springframework.web.bind.annotation.RequestMapping;\n")
                .append("import org.springframework.web.bind.annotation.PutMapping;\n")
                .append("import org.springframework.web.bind.annotation.PostMapping;\n")
                .append("import org.springframework.web.bind.annotation.GetMapping;\n")
                .append("import org.springframework.web.bind.annotation.DeleteMapping;\n")
                .append("import org.springframework.web.bind.annotation.PathVariable;\n")
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
}
