package com.github.faster.framework.builder.model;

import lombok.Data;

import java.util.List;

/**
 * 生成器所需model
 */
@Data
public class BuilderModel {
    /**
     * 项目名称
     */
    private String projectName;
    /**
     * 包路径 xxx.xx.xx
     */
    private String basePackagePath;
    /**
     * 根路径，由包路径转换为xxx/xx/xx/xx
     */
    private String basePath;
    /**
     * 依赖版本
     */
    private String dependencyVersion;

    /**
     * 表以及列数据
     */
    private List<TableColumnModel> tableColumnList;
}
