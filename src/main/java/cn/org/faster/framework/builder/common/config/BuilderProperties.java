package cn.org.faster.framework.builder.common.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;
import java.util.List;

/**
 * Builder 运行参数。
 *
 * <p>全部可通过 {@code builder.*} 配置项或同名环境变量覆盖，默认值面向"本地一键启动"。</p>
 *
 * @param jwtBase64Secret 生成单元测试用 JWT 时的 Base64 密钥，需与被生成工程的 {@code faster.base64Secret} 一致
 * @param jwtEnv          生成单元测试用 JWT 时写入的 {@code env} claim
 * @param jwtSubject      生成单元测试用 JWT 时写入的 {@code aud}（框架语义中即 userId）
 * @param connectTimeout  连接用户数据库的超时时间
 * @param queryTimeout    读取元数据的查询超时时间
 * @param skeletonMaxBytes 下载官方骨架 ZIP 的体积上限
 * @param skeletonTimeout 下载官方骨架 ZIP 的请求超时时间
 * @param projects        各生成类型对应的框架版本与骨架地址（前端从 {@code GET /builder/options} 获取）
 * @author zhangbowen
 */
@ConfigurationProperties(prefix = "builder")
public record BuilderProperties(
        String jwtBase64Secret,
        String jwtEnv,
        String jwtSubject,
        Duration connectTimeout,
        Duration queryTimeout,
        long skeletonMaxBytes,
        Duration skeletonTimeout,
        List<ProjectOption> projects
) {

    /**
     * 默认生成类型配置。骨架地址沿用历史值，可通过 {@code builder.projects} 覆盖。
     */
    private static final List<ProjectOption> DEFAULT_PROJECTS = List.of(
            new ProjectOption(1, "1.4.2.RELEASE", ""),
            new ProjectOption(2, "1.4.2.RELEASE",
                    "https://github.com/faster-framework/faster-framework-admin-api/archive/v1.0.3.RELEASE.zip"),
            new ProjectOption(3, "2.0.2.RELEASE",
                    "https://github.com/faster-framework/faster-framework-admin-web/archive/v2.0.2.RELEASE.zip"),
            new ProjectOption(4, "1.4.2.RELEASE",
                    "https://github.com/faster-framework/faster-framework-admin-api/archive/v1.0.3.RELEASE.zip"),
            new ProjectOption(5, "1.0.0.RELEASE", ""),
            new ProjectOption(6, "1.4.2.RELEASE",
                    "https://github.com/faster-framework/faster-framework-cms-api/archive/v1.0.1.RELEASE.zip"),
            new ProjectOption(7, "1.0.1.RELEASE",
                    "https://github.com/faster-framework/faster-framework-cms-web/archive/v1.0.1.RELEASE.zip"));

    public BuilderProperties {
        jwtBase64Secret = defaultIfBlank(jwtBase64Secret, "ZmFzdGVyLWZyYW1ld29yaw==");
        jwtEnv = defaultIfBlank(jwtEnv, "local");
        jwtSubject = defaultIfBlank(jwtSubject, "0");
        connectTimeout = connectTimeout == null ? Duration.ofSeconds(5) : connectTimeout;
        queryTimeout = queryTimeout == null ? Duration.ofSeconds(15) : queryTimeout;
        skeletonMaxBytes = skeletonMaxBytes <= 0 ? 64L * 1024 * 1024 : skeletonMaxBytes;
        skeletonTimeout = skeletonTimeout == null ? Duration.ofSeconds(60) : skeletonTimeout;
        projects = projects == null || projects.isEmpty() ? DEFAULT_PROJECTS : List.copyOf(projects);
    }

    /**
     * 按生成类型查找配置。
     *
     * @param type 生成类型
     * @return 对应配置，不存在时返回 {@code null}
     */
    public ProjectOption projectOf(int type) {
        return projects.stream().filter(project -> project.type() == type).findFirst().orElse(null);
    }

    private static String defaultIfBlank(String value, String defaultValue) {
        return value == null || value.isBlank() ? defaultValue : value;
    }
}
