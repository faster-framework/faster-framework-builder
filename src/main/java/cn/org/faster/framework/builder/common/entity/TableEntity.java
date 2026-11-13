package cn.org.faster.framework.builder.common.entity;

import lombok.Data;

/**
 * 表元数据。
 *
 * @author zhangbowen
 */
@Data
public class TableEntity {
    /**
     * 表名
     */
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
