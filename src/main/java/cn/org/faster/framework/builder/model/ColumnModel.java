package cn.org.faster.framework.builder.model;

import cn.org.faster.framework.builder.entity.ColumnEntity;
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
     * 首字母大写的驼峰
     */
    private String columnNameHumpUpFirst;

    /**
     * 转为javaType
     */
    private String javaType;

    /**
     * 需要引入的包
     */
    private String javaImportType;

}
