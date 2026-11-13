package cn.org.faster.framework.builder.common.token;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("JWT 生成器")
class JwtTokenGeneratorTest {

    private static final String SECRET = "ZmFzdGVyLWZyYW1ld29yaw==";

    @Test
    @DisplayName("生成三段式 token 且签名可校验")
    void createAndVerify() {
        String token = JwtTokenGenerator.createToken(SECRET, "local", "0", 0);

        assertThat(token.split("\\.")).hasSize(3);
        assertThat(JwtTokenGenerator.verify(token, SECRET)).isTrue();
        assertThat(JwtTokenGenerator.verify(token, "b3RoZXItc2VjcmV0")).isFalse();
    }

    @Test
    @DisplayName("payload 含 aud / iat / env，不写 exp")
    void payloadClaims() {
        String token = JwtTokenGenerator.createToken(SECRET, "local", "42", 0);
        String payload = new String(Base64.getUrlDecoder().decode(token.split("\\.")[1]), StandardCharsets.UTF_8);

        assertThat(payload).contains("\"aud\":\"42\"", "\"env\":\"local\"", "\"iat\":");
        assertThat(payload).doesNotContain("\"exp\":");
    }

    @Test
    @DisplayName("过期时间大于 0 时写入 exp")
    void payloadWithExpiration() {
        String token = JwtTokenGenerator.createToken(SECRET, "prod", "7", 3600);
        String payload = new String(Base64.getUrlDecoder().decode(token.split("\\.")[1]), StandardCharsets.UTF_8);

        assertThat(payload).contains("\"exp\":");
    }

    @Test
    @DisplayName("密钥非法时抛出可读异常")
    void invalidSecret() {
        assertThatThrownBy(() -> JwtTokenGenerator.createToken("not-base64!!!", "local", "0", 0))
                .hasMessageContaining("Base64");
    }
}
