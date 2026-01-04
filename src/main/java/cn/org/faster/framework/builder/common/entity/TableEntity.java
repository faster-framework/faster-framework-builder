package cn.org.faster.framework.builder.common.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;


@Data
public class TableEntity {
    /**
     * 表名
     */
    @TableId
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
