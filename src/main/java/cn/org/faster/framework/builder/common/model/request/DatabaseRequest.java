package cn.org.faster.framework.builder.common.model.request;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * 数据库连接配置。
 *
 * <p>校验只做"形状"约束（长度、字符集），主机可达性与账号正确性由 JDBC 连接阶段反馈。</p>
 *
 * @param host     数据库地址，如 {@code 127.0.0.1}
 * @param port     端口，空则默认 3306
 * @param name     数据库名
 * @param username 账号
 * @param password 密码
 * @author zhangbowen
 */
public record DatabaseRequest(
        @Pattern(regexp = "^[A-Za-z0-9._-]{1,255}$", message = "仅支持字母、数字、点、下划线、中划线")
        String host,
        @Pattern(regexp = "^[0-9]{1,5}$", message = "必须是数字")
        String port,
        @Pattern(regexp = "^[A-Za-z0-9_$-]{1,64}$", message = "仅支持字母、数字、下划线、中划线、$")
        String name,
        @Size(max = 64, message = "长度不能超过 64")
        String username,
        @Size(max = 128, message = "长度不能超过 128")
        String password
) {
}
