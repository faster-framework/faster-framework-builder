package cn.org.faster.framework.builder.common.exception;

import org.springframework.http.HttpStatus;

/**
 * 生成过程中的可预期异常，统一由 {@link BuilderExceptionHandler} 转换为 JSON 响应。
 *
 * @author zhangbowen
 */
public class BuilderException extends RuntimeException {

    private final HttpStatus status;

    public BuilderException(String message) {
        this(HttpStatus.BAD_REQUEST, message);
    }

    public BuilderException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }

    public BuilderException(HttpStatus status, String message, Throwable cause) {
        super(message, cause);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
