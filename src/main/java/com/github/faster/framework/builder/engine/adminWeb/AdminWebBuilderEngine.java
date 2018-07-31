package com.github.faster.framework.builder.engine.adminWeb;

import com.github.faster.framework.builder.engine.BuilderEngine;
import com.github.faster.framework.builder.model.ColumnModel;
import com.github.faster.framework.builder.model.TableColumnModel;
import com.github.faster.framework.builder.utils.BuilderUtils;
import com.github.faster.framework.builder.utils.FreemarkerUtils;
import com.github.faster.framework.core.utils.Utils;
import freemarker.template.Template;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class AdminWebBuilderEngine extends BuilderEngine {
    private String baseModulePath;
    private static final String ADMIN_WEB_TEMPLATE_DIR = "adminWeb";
    private Template packageJsonTemp = FreemarkerUtils.cfg.getTemplate(ADMIN_WEB_TEMPLATE_DIR + "/package.json.ftl");
    private Template AddTemp = FreemarkerUtils.cfg.getTemplate(ADMIN_WEB_TEMPLATE_DIR + "/Add.jsx.ftl");
    private Template EditTemp = FreemarkerUtils.cfg.getTemplate(ADMIN_WEB_TEMPLATE_DIR + "/Edit.jsx.ftl");
    private Template ListTemp = FreemarkerUtils.cfg.getTemplate(ADMIN_WEB_TEMPLATE_DIR + "/List.jsx.ftl");
    private Template IndexTemp = FreemarkerUtils.cfg.getTemplate(ADMIN_WEB_TEMPLATE_DIR + "/index.js.ftl");
    private Template MenuConfigTemp = FreemarkerUtils.cfg.getTemplate(ADMIN_WEB_TEMPLATE_DIR + "/menuConfig.js.ftl");
    private Template RouterConfigTemp = FreemarkerUtils.cfg.getTemplate(ADMIN_WEB_TEMPLATE_DIR + "/routerConfig.js.ftl");

    public AdminWebBuilderEngine() throws IOException {
    }


    @Override
    public byte[] start() throws IOException {
        baseModulePath = "/src/modules/";
        //创建压缩文件
//        File  zipFile= File.createTempFile("builderParam.getProjectName()", "zip");
        File zipFile = new File("/Users/zhangbowen/Documents", builderParam.getProjectName() + ".zip");
        //创建模板
        List<TableColumnModel> columnModelList = builderParam.getTableColumnList();
        try (ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(zipFile))) {
            processProject(zipOutputStream);
            for (TableColumnModel tableColumnModel : columnModelList) {
                this.processIndex(tableColumnModel, zipOutputStream);
                this.processAdd(tableColumnModel, zipOutputStream);
                this.processEdit(tableColumnModel, zipOutputStream);
                this.processList(tableColumnModel, zipOutputStream);
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
        processPackageJson(zipOutputStream);
        processMenuConfig(zipOutputStream);
        processRouterConfig(zipOutputStream);
    }

    /**
     * 生成packageJson文件
     *
     * @param zipOutputStream 压缩流
     * @throws IOException io异常
     */
    private void processPackageJson(ZipOutputStream zipOutputStream) throws IOException {
        String zipFileName = "package.json";
        zipOutputStream.putNextEntry(new ZipEntry(zipFileName));
        zipOutputStream.write(FreemarkerUtils.processIntoStream(packageJsonTemp, builderParam));
        zipOutputStream.closeEntry();
    }

    /**
     * 生成MenuConfig文件
     *
     * @param zipOutputStream 压缩流
     * @throws IOException io异常
     */
    private void processMenuConfig(ZipOutputStream zipOutputStream) throws IOException {
        String zipFileName = baseModulePath + "menuConfig.js";
        zipOutputStream.putNextEntry(new ZipEntry(zipFileName));
        zipOutputStream.write(FreemarkerUtils.processIntoStream(MenuConfigTemp, builderParam));
        zipOutputStream.closeEntry();
    }

    /**
     * 生成RouterConfig文件
     *
     * @param zipOutputStream 压缩流
     * @throws IOException io异常
     */
    private void processRouterConfig(ZipOutputStream zipOutputStream) throws IOException {
        String zipFileName = baseModulePath + "routerConfig.js";
        zipOutputStream.putNextEntry(new ZipEntry(zipFileName));
        zipOutputStream.write(FreemarkerUtils.processIntoStream(RouterConfigTemp, builderParam));
        zipOutputStream.closeEntry();
    }

    /**
     * 生成index.js
     *
     * @param zipOutputStream 压缩流
     * @throws IOException io异常
     */
    private void processIndex(TableColumnModel tableColumnModel, ZipOutputStream zipOutputStream) throws IOException {
        String zipFileName = baseModulePath + tableColumnModel.getBusinessEnName() + "/index.js";
        zipOutputStream.putNextEntry(new ZipEntry(zipFileName));
        zipOutputStream.write(FreemarkerUtils.processIntoStream(IndexTemp, tableColumnModel));
        zipOutputStream.closeEntry();
    }

    /**
     * 生成add
     *
     * @param zipOutputStream 压缩流
     * @throws IOException io异常
     */
    private void processAdd(TableColumnModel tableColumnModel, ZipOutputStream zipOutputStream) throws IOException {
        String zipFileName = baseModulePath + tableColumnModel.getBusinessEnName() + "/" + tableColumnModel.getBusinessEnNameUpFirst() + "Add.jsx";
        List<ColumnModel> columnList = tableColumnModel.getColumnList().stream()
                .filter(item -> BuilderUtils.baseNotContainsProperty(item.getColumnNameHump()))
                .collect(Collectors.toList());
        Map<String, Object> map = Utils.beanToMap(tableColumnModel);
        map.put("columnList", columnList);
        zipOutputStream.putNextEntry(new ZipEntry(zipFileName));
        zipOutputStream.write(FreemarkerUtils.processIntoStream(AddTemp, map));
        zipOutputStream.closeEntry();
    }

    /**
     * 生成edit
     *
     * @param zipOutputStream 压缩流
     * @throws IOException io异常
     */
    private void processEdit(TableColumnModel tableColumnModel, ZipOutputStream zipOutputStream) throws IOException {
        String zipFileName = baseModulePath + tableColumnModel.getBusinessEnName() + "/" + tableColumnModel.getBusinessEnNameUpFirst() + "Edit.jsx";
        List<ColumnModel> columnList = tableColumnModel.getColumnList().stream()
                .filter(item -> BuilderUtils.baseNotContainsProperty(item.getColumnNameHump()))
                .collect(Collectors.toList());
        Map<String, Object> map = Utils.beanToMap(tableColumnModel);
        map.put("columnList", columnList);
        zipOutputStream.putNextEntry(new ZipEntry(zipFileName));
        zipOutputStream.write(FreemarkerUtils.processIntoStream(EditTemp, map));
        zipOutputStream.closeEntry();
    }

    /**
     * 生成List
     *
     * @param zipOutputStream 压缩流
     * @throws IOException io异常
     */
    private void processList(TableColumnModel tableColumnModel, ZipOutputStream zipOutputStream) throws IOException {
        String zipFileName = baseModulePath + tableColumnModel.getBusinessEnName() + "/" + tableColumnModel.getBusinessEnNameUpFirst() + "List.jsx";
        List<ColumnModel> columnList = tableColumnModel.getColumnList().stream()
                .filter(item -> BuilderUtils.baseNotContainsProperty(item.getColumnNameHump()))
                .collect(Collectors.toList());
        Map<String, Object> map = Utils.beanToMap(tableColumnModel);
        map.put("columnList", columnList);
        zipOutputStream.putNextEntry(new ZipEntry(zipFileName));
        zipOutputStream.write(FreemarkerUtils.processIntoStream(ListTemp, map));
        zipOutputStream.closeEntry();
    }
}
