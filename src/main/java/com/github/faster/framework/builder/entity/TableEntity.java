package com.github.faster.framework.builder.entity;

import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Table(name = "tables")
public class TableEntity {
    /**
     * 表名
     */
    @Id
    private String tableName;
    /**
     * 数据库名称
     */
    private String tableSchema;
    /**
     * 表注释
     */
    private String tableComment;

}
