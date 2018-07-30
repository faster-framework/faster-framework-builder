package com.github.faster.framework.builder.engine.api;

import com.github.faster.framework.builder.engine.JavaBuilderEngine;
import com.github.faster.framework.builder.model.TableColumnModel;
import com.github.faster.framework.builder.utils.FreemarkerUtils;
import freemarker.template.Template;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipOutputStream;

public class ApiBuilderEngine extends JavaBuilderEngine {
    private static final String API_TEMPLATE_DIR = "api";
    private Template entityTemp = FreemarkerUtils.cfg.getTemplate(API_TEMPLATE_DIR + "/entity.ftl");
    private Template mapperTemp = FreemarkerUtils.cfg.getTemplate(API_TEMPLATE_DIR + "/mapper.ftl");
    private Template serviceTemp = FreemarkerUtils.cfg.getTemplate(API_TEMPLATE_DIR + "/service.ftl");
    private Template controllerTemp = FreemarkerUtils.cfg.getTemplate(API_TEMPLATE_DIR + "/controller.ftl");

    public ApiBuilderEngine() throws IOException {
    }

    @Override
    public byte[] start() throws IOException {
        //创建压缩文件
//        File  zipFile= File.createTempFile("builderParam.getProjectName()", "zip");
        File zipFile = new File("/Users/zhangbowen/Documents", builderParam.getProjectName() + ".zip");
        processProject();
        //创建模板
        List<TableColumnModel> columnModelList = builderParam.getTableColumnList();
        try (ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(zipFile))) {
            for (TableColumnModel tableColumnModel : columnModelList) {
                super.processEntity(entityTemp, tableColumnModel, zipOutputStream);
                super.processMapper(mapperTemp, tableColumnModel, zipOutputStream);
                super.processService(serviceTemp, tableColumnModel, zipOutputStream);
//                processModules(controllerTemp, tableColumnModel, zipOutputStream);
            }
        }

        return new byte[0];
    }


    private void processProject() {

    }

}
