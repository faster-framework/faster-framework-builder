package cn.org.faster.framework.builder.common.entity;

import lombok.Data;

/**
 * 列元数据。
 *
 * @author zhangbowen
 */
@Data
public class ColumnEntity {
    /**
     * 列名称
     */
    private String columnName;
    /**
     * 数据类型（information_schema.COLUMNS.DATA_TYPE）
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
    /**
     * 是否为空（NO / YES）
     */
    private String isNullable;
}
