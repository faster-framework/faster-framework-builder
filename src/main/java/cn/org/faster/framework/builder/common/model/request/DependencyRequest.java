package cn.org.faster.framework.builder.common.model.request;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * 依赖版本与官方骨架下载地址。
 *
 * <p>由前端根据项目类型下发；后端仍会校验 URL 协议与版本号形状。</p>
 *
 * @param version 框架依赖版本，如 {@code 1.4.2.RELEASE}
 * @param url     骨架 ZIP 地址（http/https）
 * @author zhangbowen
 */
public record DependencyRequest(
        @Pattern(regexp = "^[A-Za-z0-9._-]{1,32}$", message = "版本号格式不合法")
        String version,
        @Size(max = 512, message = "长度不能超过 512")
        String url
) {
}
