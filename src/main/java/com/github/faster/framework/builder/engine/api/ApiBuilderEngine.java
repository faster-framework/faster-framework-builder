package com.github.faster.framework.builder.engine.api;

import com.github.faster.framework.builder.engine.BuilderEngine;
import com.github.faster.framework.builder.model.TableColumnModel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ApiBuilderEngine extends BuilderEngine {
    @Override
    public byte[] start() {
        try {
            File zipFile = new File("/Users/zhangbowen/Documents", builderParam.getProjectName() + ".zip");
            System.out.println(zipFile.getParentFile().exists());
            if(!zipFile.exists()){
                zipFile.createNewFile();
            }
            List<TableColumnModel> columnModelList = builderParam.getTableColumnList();
            ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(zipFile));
            for (TableColumnModel tableColumnModel : columnModelList) {
                zipOutputStream.putNextEntry(new ZipEntry(builderParam.getBasePath() + "/" + tableColumnModel.getBusinessEnName() + ".java"));
                zipOutputStream.closeEntry();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new byte[0];
    }
}
