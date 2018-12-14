package cn.org.faster.framework.builder.common.service;

import cn.org.faster.framework.builder.common.config.web.context.MultipleDataSourceContext;
import cn.org.faster.framework.builder.common.constants.BuilderConstants;
import cn.org.faster.framework.builder.common.entity.TableEntity;
import cn.org.faster.framework.builder.common.mapper.TableMapper;
import cn.org.faster.framework.builder.common.model.BuilderModel;
import cn.org.faster.framework.builder.common.model.ColumnModel;
import cn.org.faster.framework.builder.common.model.TableColumnModel;
import cn.org.faster.framework.builder.common.model.request.BuilderRequest;
import cn.org.faster.framework.builder.common.utils.BuilderUtils;
import cn.org.faster.framework.builder.modules.context.ApiContext;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class BuilderService {
    private TableMapper tableMapper;
    private HttpServletResponse httpServletResponse;

    /**
     * @param builderRequest 请求参数
     * @return 获取本次需要生成的表以及列数据
     */
    private List<TableColumnModel> selectTableWithColumn(BuilderRequest builderRequest) {
        //查询要生成的表,以及列信息
        TableEntity queryTable = new TableEntity();
        queryTable.setTableName(builderRequest.getBusiness().getTableName());
        queryTable.setTableSchema(builderRequest.getDatabase().getName());
        return tableMapper.selectWithColumn(queryTable);
    }

    /**
     * @param projectName 项目名称
     * @param outputBytes 输出下载的字节
     * @throws IOException 写回下载流
     */
    private void outputDownloadStream(String projectName, byte[] outputBytes) throws IOException {
        String fileName = projectName + ".zip";
        httpServletResponse.setContentType("application/octet-stream; charset=utf-8");
        httpServletResponse.setHeader("Content-Disposition", "attachment;filename="
                .concat(String.valueOf(URLEncoder.encode(fileName, "UTF-8"))));
        httpServletResponse.setStatus(201);
        try (OutputStream outputStream = httpServletResponse.getOutputStream()) {
            outputStream.write(outputBytes);
            outputStream.flush();
        }
    }

    /**
     * 生成服务主入口
     *
     * @param builderRequest 请求参数
     */
    public void build(BuilderRequest builderRequest) throws IOException {
        //设置数据源
        MultipleDataSourceContext.setMultipleDataSourceThreadLocal(builderRequest.getDatabase());
        //获取本次需要生成的表以及列数据
        List<TableColumnModel> tableColumnModelList = selectTableWithColumn(builderRequest);
        byte[] zipBytes = process(builderRequest, tableColumnModelList);
        //写回下载流
        outputDownloadStream(builderRequest.getBusiness().getProjectName(), zipBytes);
    }

    /**
     * @param builderRequest  请求参数
     * @param tableColumnList 要生成的数据表，携带列信息
     * @return 具体的生成器引擎
     */
    private byte[] process(BuilderRequest builderRequest, List<TableColumnModel> tableColumnList) throws IOException {
        BuilderModel builderModel = initBuildModel(builderRequest, tableColumnList);
        switch (builderRequest.getType()) {
            case BuilderConstants
                    .API:
                return new ApiContext(builderModel).process();
            case BuilderConstants
                    .ADMIN_API:
                return new ApiContext(builderModel).process();
            case BuilderConstants
                    .ADMIN_WEB:
                return new ApiContext(builderModel).process();
            case BuilderConstants
                    .ADMIN_PERMISSION:
                return new ApiContext(builderModel).process();
            default:
                return new ApiContext(builderModel).process();
        }
    }

    /**
     * @param columnList 列
     * @return 完善列信息
     */
    private List<ColumnModel> completeColumn(List<ColumnModel> columnList) {
        return columnList.stream().peek(item -> {
            item.setColumnComment(item.getColumnComment().replaceAll("\\s", ""));
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
    private List<TableColumnModel> completeTableColumn(List<TableColumnModel> tableColumnList) {
        return tableColumnList.parallelStream().peek(item -> {
            item.setBusinessEnName(BuilderUtils.delPrefixAndHump(item.getTableName()));
            item.setBusinessEnNameUpFirst(BuilderUtils.firstCharToUpperCase(item.getBusinessEnName()));
            item.setBusinessCnName(StringUtils.isEmpty(item.getTableComment()) ? item.getBusinessEnName() : BuilderUtils.filterBusinessCnName(item.getTableComment()));
            item.setColumnList(completeColumn(item.getColumnList()));
        }).collect(Collectors.toList());
    }

    /**
     * 初始化
     *
     * @param builderRequest  请求参数
     * @param tableColumnList 表、列
     */
    private BuilderModel initBuildModel(BuilderRequest builderRequest, List<TableColumnModel> tableColumnList) {
        BuilderModel builderParam = new BuilderModel();
        builderParam.setDbHost(builderRequest.getDatabase().getHost());
        builderParam.setDbName(builderRequest.getDatabase().getName());
        builderParam.setDbPort(builderRequest.getDatabase().getPort());
        builderParam.setDbPwd(builderRequest.getDatabase().getPassword());
        builderParam.setDbUsername(builderRequest.getDatabase().getUsername());
        builderParam.setProjectName(builderRequest.getBusiness().getProjectName());
        builderParam.setTableColumnList(completeTableColumn(tableColumnList));
        builderParam.setBasePackagePath(builderRequest.getBusiness().getBasePackagePath());
        builderParam.setBasePath(BuilderUtils.packagePathToPath(builderRequest.getBusiness().getBasePackagePath()));
        if (builderRequest.getDependency() != null) {
            builderParam.setDependencyVersion(builderRequest.getDependency().getVersion());
            builderParam.setDependencyUrl(builderRequest.getDependency().getUrl());
        }
        return builderParam;
    }


}
