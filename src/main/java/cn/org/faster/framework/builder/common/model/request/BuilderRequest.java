package cn.org.faster.framework.builder.common.model.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/**
 * 代码生成请求。
 *
 * @param database   数据库配置（type=5/6/7 时可为空）
 * @param business   业务配置
 * @param dependency 依赖版本与骨架下载地址
 * @param type       生成类型：1 接口、2 管理端接口、3 管理端前端、4 合一、5 SQL、6 CMS 接口、7 CMS 前端
 * @author zhangbowen
 */
public record BuilderRequest(
        @Valid DatabaseRequest database,
        @NotNull(message = "不能为空") @Valid BusinessRequest business,
        @Valid DependencyRequest dependency,
        @NotNull(message = "不能为空") @Min(value = 1, message = "取值范围 1-7") @Max(value = 7, message = "取值范围 1-7") Integer type
) {
}
