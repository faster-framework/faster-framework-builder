package cn.org.faster.framework.builder.common.metadata;

import cn.org.faster.framework.builder.common.config.BuilderProperties;
import cn.org.faster.framework.builder.common.exception.BuilderException;
import cn.org.faster.framework.builder.common.model.request.DatabaseRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("JDBC 连接参数构造与校验")
class JdbcEndpointTest {

    private final BuilderProperties properties = new BuilderProperties(
            null, null, null, Duration.ofSeconds(3), Duration.ofSeconds(9), 0, null, null);

    @Test
    @DisplayName("正常参数生成带超时的 information_schema URL")
    void buildUrl() {
        String url = JdbcEndpoint.buildUrl(database("127.0.0.1", "3307", "demo"), properties);

        assertThat(url).startsWith("jdbc:mysql://127.0.0.1:3307/information_schema?")
                .contains("connectTimeout=3000", "socketTimeout=9000",
                        "allowMultiQueries=false", "allowLoadLocalInfile=false");
    }

    @Test
    @DisplayName("端口为空时使用 3306")
    void defaultPort() {
        assertThat(JdbcEndpoint.buildUrl(database("db.local", null, "demo"), properties))
                .contains(":3306/");
    }

    @Test
    @DisplayName("拒绝非法主机名，避免 URL 参数注入")
    void rejectInjectedHost() {
        assertThatThrownBy(() -> JdbcEndpoint.buildUrl(database("127.0.0.1:3306/?allowLoadLocalInfile=true#", "3306", "demo"), properties))
                .isInstanceOf(BuilderException.class)
                .hasMessageContaining("数据库地址不合法");
    }

    @Test
    @DisplayName("拒绝越界端口与非数字端口")
    void rejectBadPort() {
        assertThatThrownBy(() -> JdbcEndpoint.buildUrl(database("127.0.0.1", "70000", "demo"), properties))
                .hasMessageContaining("1-65535");
        assertThatThrownBy(() -> JdbcEndpoint.buildUrl(database("127.0.0.1", "abc", "demo"), properties))
                .hasMessageContaining("必须是数字");
    }

    @Test
    @DisplayName("拒绝非法库名")
    void rejectBadSchema() {
        assertThatThrownBy(() -> JdbcEndpoint.buildUrl(database("127.0.0.1", "3306", "demo;drop"), properties))
                .hasMessageContaining("数据库名称不合法");
    }

    private static DatabaseRequest database(String host, String port, String name) {
        return new DatabaseRequest(host, port, name, "root", "secret");
    }
}
