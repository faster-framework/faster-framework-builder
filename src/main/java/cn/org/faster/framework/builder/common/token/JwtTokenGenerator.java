package cn.org.faster.framework.builder.common.token;

import cn.org.faster.framework.builder.common.exception.BuilderException;

import tools.jackson.databind.json.JsonMapper;
import org.springframework.http.HttpStatus;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.time.Instant;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 生成单元测试用的 HS256 JWT（JDK 原生实现，不引入 jjwt / javax.xml.bind）。
 *
 * <p>与框架 {@code JwtService#createToken} 产出的结构保持一致：
 * header {@code {"alg":"HS256","typ":"JWT"}}，payload 含 {@code aud / iat / env}，
 * 密钥为 Base64 解码后的字节。</p>
 *
 * @author zhangbowen
 */
public final class JwtTokenGenerator {

    private static final JsonMapper MAPPER = JsonMapper.builder().build();
    private static final Base64.Encoder ENCODER = Base64.getUrlEncoder().withoutPadding();
    private static final String HMAC_ALGORITHM = "HmacSHA256";
    private static final String JWT_HEADER = "{\"alg\":\"HS256\",\"typ\":\"JWT\"}";

    private JwtTokenGenerator() {
    }

    /**
     * 生成 token。
     *
     * @param base64Secret Base64 编码的密钥
     * @param env          写入 {@code env} claim 的环境标识
     * @param audience     {@code aud} claim（框架语义中为用户 id）
     * @param expSeconds   过期秒数，小于等于 0 表示不写 {@code exp}
     * @return JWT 字符串
     */
    public static String createToken(String base64Secret, String env, String audience, long expSeconds) {
        if (base64Secret == null || base64Secret.isBlank()) {
            throw new BuilderException(HttpStatus.INTERNAL_SERVER_ERROR, "未配置 JWT 密钥");
        }
        byte[] key = decodeSecret(base64Secret);
        Map<String, Object> payload = new LinkedHashMap<>();
        long nowSeconds = Instant.now().getEpochSecond();
        payload.put("aud", audience);
        payload.put("iat", nowSeconds);
        payload.put("env", env);
        if (expSeconds > 0) {
            payload.put("exp", nowSeconds + expSeconds);
        }
        String signingInput = encode(JWT_HEADER.getBytes(StandardCharsets.UTF_8)) + "."
                + encode(toJson(payload).getBytes(StandardCharsets.UTF_8));
        return signingInput + "." + encode(hmacSha256(signingInput.getBytes(StandardCharsets.US_ASCII), key));
    }

    /**
     * 校验签名，供测试与自检使用。
     *
     * @param token        JWT
     * @param base64Secret Base64 编码的密钥
     * @return 签名是否匹配
     */
    public static boolean verify(String token, String base64Secret) {
        int lastDot = token.lastIndexOf('.');
        if (lastDot <= 0) {
            return false;
        }
        String signingInput = token.substring(0, lastDot);
        byte[] expected = hmacSha256(signingInput.getBytes(StandardCharsets.US_ASCII), decodeSecret(base64Secret));
        return encode(expected).equals(token.substring(lastDot + 1));
    }

    private static byte[] decodeSecret(String base64Secret) {
        try {
            return Base64.getDecoder().decode(base64Secret);
        } catch (IllegalArgumentException ex) {
            throw new BuilderException(HttpStatus.INTERNAL_SERVER_ERROR, "JWT 密钥不是合法的 Base64 字符串", ex);
        }
    }

    private static byte[] hmacSha256(byte[] content, byte[] key) {
        try {
            Mac mac = Mac.getInstance(HMAC_ALGORITHM);
            mac.init(new SecretKeySpec(key, HMAC_ALGORITHM));
            return mac.doFinal(content);
        } catch (GeneralSecurityException ex) {
            throw new BuilderException(HttpStatus.INTERNAL_SERVER_ERROR, "生成 JWT 签名失败", ex);
        }
    }

    private static String toJson(Map<String, Object> payload) {
        try {
            return MAPPER.writeValueAsString(payload);
        } catch (RuntimeException ex) {
            throw new BuilderException(HttpStatus.INTERNAL_SERVER_ERROR, "序列化 JWT payload 失败", ex);
        }
    }

    private static String encode(byte[] bytes) {
        return ENCODER.encodeToString(bytes);
    }
}
