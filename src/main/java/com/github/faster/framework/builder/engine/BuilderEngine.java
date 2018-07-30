package com.github.faster.framework.builder.engine;

import com.github.faster.framework.builder.constants.BuilderTypeConstants;
import com.github.faster.framework.builder.engine.adminApi.AdminApiBuilderEngine;
import com.github.faster.framework.builder.engine.adminWeb.AdminWebBuilderEngine;
import com.github.faster.framework.builder.engine.api.ApiBuilderEngine;
import com.github.faster.framework.builder.model.BuilderModel;
import com.github.faster.framework.builder.model.ColumnModel;
import com.github.faster.framework.builder.model.TableColumnModel;
import com.github.faster.framework.builder.model.request.BuilderRequest;
import com.github.faster.framework.builder.utils.BuilderUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public abstract class BuilderEngine {
    protected BuilderModel builderParam;

    /**
     * @param columnList 列
     * @return 完善列信息
     */
    protected List<ColumnModel> completeColumn(List<ColumnModel> columnList) {
        return columnList.stream().peek(item -> {
            item.setColumnNameHump(BuilderUtils.hump(item.getColumnName()));
            item.setColumnNameHumpUpFirst(BuilderUtils.firstCharToUpperCase(item.getColumnNameHump()));
            item.setJavaType(BuilderUtils.convertToJavaType(item.getDataType()));
            item.setJavaImportType(BuilderUtils.javaImportType(item.getJavaType()));
        }).collect(Collectors.toList());
    }

    /**
     * @param tableColumnList 要生成的表以及列的数据
     * @return 完善tableColumn中所需参数
     */
    protected List<TableColumnModel> completeTableColumn(List<TableColumnModel> tableColumnList) {
        return tableColumnList.parallelStream().peek(item -> {
            item.setBusinessEnName(BuilderUtils.delPrefixAndHump(item.getTableName()));
            item.setBusinessEnNameUpFirst(BuilderUtils.firstCharToUpperCase(item.getBusinessEnName()));
            item.setBusinessCnName(StringUtils.isEmpty(item.getTableComment()) ? item.getBusinessEnName() : BuilderUtils.filterBusinessCnName(item.getTableComment()));
            item.setColumnList(this.completeColumn(item.getColumnList()));
        }).collect(Collectors.toList());
    }

    /**
     * 初始化
     *
     * @param builderRequest  请求参数
     * @param tableColumnList 表、列
     */
    protected void init(BuilderRequest builderRequest, List<TableColumnModel> tableColumnList) {
        builderParam = new BuilderModel();
        builderParam.setDbHost(builderRequest.getDatabase().getHost());
        builderParam.setDbName(builderRequest.getDatabase().getName());
        builderParam.setDbPort(builderRequest.getDatabase().getPort());
        builderParam.setDbPwd(builderRequest.getDatabase().getPassword());
        builderParam.setDbUsername(builderRequest.getDatabase().getUsername());
        builderParam.setDependencyVersion(builderRequest.getDependency().getVersion());
        builderParam.setProjectName(builderRequest.getBusiness().getProjectName());
        builderParam.setTableColumnList(completeTableColumn(tableColumnList));
        builderParam.setBasePackagePath(builderRequest.getBusiness().getBasePackagePath());
        builderParam.setBasePath(BuilderUtils.packagePathToPath(builderRequest.getBusiness().getBasePackagePath()));
    }

    /**
     * @param builderRequest  请求参数
     * @param tableColumnList 要生成的数据表，携带列信息
     * @return 具体的生成器引擎
     */
    public static BuilderEngine build(BuilderRequest builderRequest, List<TableColumnModel> tableColumnList) throws IOException {
        BuilderEngine builderEngine;
        switch (builderRequest.getType()) {
            case BuilderTypeConstants
                    .API:
                builderEngine = new ApiBuilderEngine();
                break;
            case BuilderTypeConstants
                    .ADMIN_API:
                builderEngine = new AdminApiBuilderEngine();
                break;
            case BuilderTypeConstants
                    .ADMIN_WEB:
                builderEngine = new AdminWebBuilderEngine();
                break;
            default:
                builderEngine = new ApiBuilderEngine();
        }
        builderEngine.init(builderRequest, tableColumnList);
        return builderEngine;
    }

    public abstract byte[] start() throws IOException;
}
