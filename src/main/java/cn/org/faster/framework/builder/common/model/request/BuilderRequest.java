package cn.org.faster.framework.builder.common.model.request;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Data
public class BuilderRequest {
    /**
     * 数据库配置
     */
    private DatabaseRequest database;

    /**
     * 业务配置
     */
    private BusinessRequest business;

    /**
     * 引入包的版本
     */
    private DependencyRequest dependency;

    /**
     * 生成类型
     */
    private Integer type;


}
