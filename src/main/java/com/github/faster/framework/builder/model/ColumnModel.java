package com.github.faster.framework.builder.model;

import com.github.faster.framework.builder.entity.ColumnEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ColumnModel extends ColumnEntity {
    /**
     * 驼峰列
     */
    private String columnNameHump;

    /**
     * 由dataType转换为java类型
     */
    private String javaType;

}
