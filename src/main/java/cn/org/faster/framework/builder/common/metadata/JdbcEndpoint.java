package cn.org.faster.framework.builder.common.metadata;

import cn.org.faster.framework.builder.common.config.BuilderProperties;
import cn.org.faster.framework.builder.common.exception.BuilderException;
import cn.org.faster.framework.builder.common.model.request.DatabaseRequest;
import org.springframework.http.HttpStatus;

import java.util.regex.Pattern;

/**
 * 根据用户提交的连接信息构造 JDBC URL。
 *
 * <p>只做一件事：把不可信的 host/port 变成合法 URL，账号密码通过 {@link java.util.Properties} 传递，
 * 不拼接进 URL 查询串（避免 {@code ?allowLoadLocalInfile=true} 之类的参数注入）。</p>
 *
 * @author zhangbowen
 */
public final class JdbcEndpoint {

    /**
     * 允许的主机名：字母、数字、点、下划线、中划线（不支持 IPv6 字面量，如需请用域名）。
     */
    private static final Pattern HOST_PATTERN = Pattern.compile("^[A-Za-z0-9._-]{1,255}$");
    /**
     * 允许的库名。
     */
    private static final Pattern SCHEMA_PATTERN = Pattern.compile("^[A-Za-z0-9_$-]{1,64}$");
    private static final int MIN_PORT = 1;
    private static final int MAX_PORT = 65535;

    private JdbcEndpoint() {
    }

    /**
     * 校验并构造 JDBC URL。
     *
     * @param database 用户提交的数据库连接信息
     * @param properties 运行参数（超时时间）
     * @return 指向 information_schema 的 JDBC URL
     */
    public static String buildUrl(DatabaseRequest database, BuilderProperties properties) {
        String host = requireMatch(database.host(), HOST_PATTERN, "数据库地址不合法");
        int port = parsePort(database.port());
        requireMatch(database.name(), SCHEMA_PATTERN, "数据库名称不合法");
        long connectTimeoutMillis = properties.connectTimeout().toMillis();
        long socketTimeoutMillis = properties.queryTimeout().toMillis();
        return "jdbc:mysql://" + host + ":" + port + "/information_schema"
                + "?connectTimeout=" + connectTimeoutMillis
                + "&socketTimeout=" + socketTimeoutMillis
                + "&sslMode=DISABLED"
                + "&allowPublicKeyRetrieval=true"
                + "&allowMultiQueries=false"
                + "&allowLoadLocalInfile=false"
                + "&connectionTimeZone=SERVER";
    }

    static int parsePort(String port) {
        if (port == null || port.isBlank()) {
            return 3306;
        }
        try {
            int value = Integer.parseInt(port.trim());
            if (value < MIN_PORT || value > MAX_PORT) {
                throw new BuilderException(HttpStatus.BAD_REQUEST, "数据库端口需在 1-65535 之间");
            }
            return value;
        } catch (NumberFormatException ex) {
            throw new BuilderException(HttpStatus.BAD_REQUEST, "数据库端口必须是数字");
        }
    }

    private static String requireMatch(String value, Pattern pattern, String message) {
        if (value == null || !pattern.matcher(value).matches()) {
            throw new BuilderException(HttpStatus.BAD_REQUEST, message);
        }
        return value;
    }
}
