package com.github.faster.framework.builder.model.request;

import com.github.faster.framework.builder.constants.BuilderTypeEnum;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Data
public class BuilderRequest {
    /**
     * 数据库配置
     */
    @Valid
    @NotNull
    private DatabaseRequest database;

    /**
     * 业务配置
     */
    @Valid
    @NotNull
    private BusinessRequest business;

    /**
     * 引入包的版本
     */
    private DependencyRequest dependencyRequest;

    /**
     * 生成类型
     */
    @NotNull
    private BuilderTypeEnum type;


}
