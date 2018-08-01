package com.github.faster.framework.builder.service;

import com.github.faster.framework.builder.config.web.context.MultipleDataSourceContext;
import com.github.faster.framework.builder.engine.BuilderEngine;
import com.github.faster.framework.builder.entity.TableEntity;
import com.github.faster.framework.builder.mapper.TableMapper;
import com.github.faster.framework.builder.model.TableColumnModel;
import com.github.faster.framework.builder.model.request.BuilderRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

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
        String fileName = projectName + "-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) + ".zip";
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
        byte[] zipBytes = BuilderEngine.build(builderRequest, tableColumnModelList).start();
        //写回下载流
        outputDownloadStream(builderRequest.getBusiness().getProjectName(), zipBytes);
    }
}
