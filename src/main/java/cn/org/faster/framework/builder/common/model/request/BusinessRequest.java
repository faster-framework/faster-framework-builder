package cn.org.faster.framework.builder.common.model.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 业务模块配置请求
 */
@Data
public class BusinessRequest {
    /**
     * 项目名称
     */
    private String projectName;
    /**
     * 包路径
     */
    private String basePackagePath;
    /**
     * 表名
     */
    private String tableName;

}
