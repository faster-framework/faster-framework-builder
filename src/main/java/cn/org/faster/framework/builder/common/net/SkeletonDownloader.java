package cn.org.faster.framework.builder.common.net;

import cn.org.faster.framework.builder.common.exception.BuilderException;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

/**
 * 骨架 ZIP 下载器：JDK 原生 {@link HttpClient}，替代 {@code RestTemplate}。
 *
 * <p>相比原实现：不落临时文件、带连接/请求超时、限制下载体积、校验 URL 协议。</p>
 *
 * @author zhangbowen
 */
public final class SkeletonDownloader {

    private static final HttpClient CLIENT = HttpClient.newBuilder()
            .followRedirects(HttpClient.Redirect.NORMAL)
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    private SkeletonDownloader() {
    }

    /**
     * 下载并返回全量字节。
     *
     * @param url      下载地址（http/https）
     * @param timeout  请求超时
     * @param maxBytes 允许的最大体积
     * @return ZIP 字节内容
     */
    public static byte[] download(String url, Duration timeout, long maxBytes) {
        URI uri = parse(url);
        HttpRequest request = HttpRequest.newBuilder(uri)
                .timeout(timeout)
                .header("Accept", "application/zip, application/octet-stream, */*")
                .header("User-Agent", "faster-framework-builder")
                .GET()
                .build();
        try {
            HttpResponse<InputStream> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofInputStream());
            if (response.statusCode() / 100 != 2) {
                closeQuietly(response.body());
                throw new BuilderException(HttpStatus.BAD_GATEWAY,
                        "下载官方骨架失败，HTTP " + response.statusCode() + "：" + url);
            }
            try (InputStream body = response.body()) {
                return readLimited(body, maxBytes);
            }
        } catch (IOException ex) {
            throw new BuilderException(HttpStatus.BAD_GATEWAY, "下载官方骨架失败：" + ex.getMessage(), ex);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            throw new BuilderException(HttpStatus.BAD_GATEWAY, "下载官方骨架被中断", ex);
        }
    }

    private static URI parse(String url) {
        if (url == null || url.isBlank()) {
            throw new BuilderException(HttpStatus.BAD_REQUEST, "未指定官方骨架下载地址");
        }
        try {
            URI uri = new URI(url.trim());
            String scheme = uri.getScheme();
            if (scheme == null || (!scheme.equalsIgnoreCase("http") && !scheme.equalsIgnoreCase("https"))) {
                throw new BuilderException(HttpStatus.BAD_REQUEST, "骨架地址仅支持 http/https：" + url);
            }
            if (uri.getHost() == null) {
                throw new BuilderException(HttpStatus.BAD_REQUEST, "骨架地址缺少主机名：" + url);
            }
            return uri;
        } catch (URISyntaxException ex) {
            throw new BuilderException(HttpStatus.BAD_REQUEST, "骨架地址非法：" + url, ex);
        }
    }

    private static byte[] readLimited(InputStream input, long maxBytes) throws IOException {
        var output = new java.io.ByteArrayOutputStream(256 * 1024);
        byte[] buffer = new byte[8192];
        long total = 0;
        int read;
        while ((read = input.read(buffer)) != -1) {
            total += read;
            if (total > maxBytes) {
                throw new BuilderException(HttpStatus.BAD_GATEWAY,
                        "官方骨架体积超过限制（" + maxBytes + " 字节）");
            }
            output.write(buffer, 0, read);
        }
        return output.toByteArray();
    }

    private static void closeQuietly(InputStream input) {
        try {
            input.close();
        } catch (IOException ignored) {
            // 忽略关闭异常
        }
    }
}
