package com.github.faster.framework.builder.model;

import com.github.faster.framework.builder.entity.TableEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class TableColumnModel extends TableEntity {
    /**
     * 业务英文名称：表名去除前缀，去除下划线，驼峰
     */
    private String businessEnName;
    /**
     * 业务英文名称，首字母大写：表名去除前缀，去除下划线，驼峰
     */
    private String businessEnNameUpFirst;
    /**
     * 业务中文名称：表的注释，如果没有，以业务英文名称为主
     */
    private String businessCnName;
    /**
     * 列信息
     */
    private List<ColumnModel> columnList;
}
