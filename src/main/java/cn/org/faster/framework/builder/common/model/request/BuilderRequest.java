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
    @Valid
    @NotNull(message = "数据库参数不能为空")
    private DatabaseRequest database;

    /**
     * 业务配置
     */
    @Valid
    @NotNull(message = "业务参数不能为空")
    private BusinessRequest business;

    /**
     * 引入包的版本
     */
    private DependencyRequest dependency;

    /**
     * 生成类型
     */
    @NotNull(message = "类型不能为空")
    @Range(min = 1, max = 5, message = "类型选择错误")
    private Integer type;


}
