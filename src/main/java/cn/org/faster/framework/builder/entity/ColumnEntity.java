package cn.org.faster.framework.builder.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;


@Data
public class ColumnEntity {
    /**
     * 列名称
     */
    @TableId
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

    /**
     * 是否为空（NO，YES)
     */
    private String isNullable;
}
