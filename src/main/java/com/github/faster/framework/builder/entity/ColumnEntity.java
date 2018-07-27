package com.github.faster.framework.builder.entity;

import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Table(name = "columns")
public class ColumnEntity {
    /**
     * 列名称
     */
    @Id
    private String columnName;
    /**
     * 数据类型
     */
    private String dataType;
    /**
     * 排序
     */
    private Integer ordinalPosition;
    /**
     * 注释
     */
    private String columnComment;
}
