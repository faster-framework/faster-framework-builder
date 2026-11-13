package cn.org.faster.framework.builder.common.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * 业务模块配置。
 *
 * @param projectName     项目名，同时作为下载文件名
 * @param basePackagePath 根包路径，如 {@code com.example}
 * @param tableName       表名；为空表示生成整库
 * @author zhangbowen
 */
public record BusinessRequest(
        @NotBlank(message = "不能为空")
        @Pattern(regexp = "^[\\p{L}\\p{N}._-]{1,64}$", message = "仅支持中英文、数字、点、下划线、中划线，长度 1-64")
        String projectName,
        @Pattern(regexp = "^[a-zA-Z_][a-zA-Z0-9_]*(\\.[a-zA-Z_][a-zA-Z0-9_]*)*$", message = "必须是合法的包名，如 com.example")
        @Size(max = 128, message = "长度不能超过 128")
        String basePackagePath,
        @Pattern(regexp = "^[A-Za-z0-9_$]{1,64}$", message = "仅支持字母、数字、下划线、$")
        String tableName
) {
}
