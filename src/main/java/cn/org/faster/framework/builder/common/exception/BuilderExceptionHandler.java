package cn.org.faster.framework.builder.common.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

/**
 * 统一错误响应：{@code {"code":400,"message":"..."}}。
 *
 * <p>模板渲染、数据库连接、骨架下载失败都会走到这里，避免出现"返回 200 但 ZIP 是空的"。</p>
 *
 * @author zhangbowen
 */
@RestControllerAdvice
public class BuilderExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(BuilderExceptionHandler.class);

    @ExceptionHandler(BuilderException.class)
    public ResponseEntity<ErrorResponse> handleBuilder(BuilderException ex) {
        log.warn("生成失败: {}", ex.getMessage());
        return ResponseEntity.status(ex.getStatus())
                .body(new ErrorResponse(ex.getStatus().value(), ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + " " + error.getDefaultMessage())
                .collect(Collectors.joining("；"));
        log.warn("参数校验失败: {}", message);
        return ResponseEntity.badRequest()
                .body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), message.isBlank() ? "请求参数不合法" : message));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpected(Exception ex) {
        log.error("生成器内部错误", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "生成器内部错误: " + ex.getMessage()));
    }

    /**
     * 错误响应体。
     *
     * @param code    业务状态码，与 HTTP 状态一致
     * @param message 可读的错误信息
     */
    public record ErrorResponse(int code, String message) {
    }
}
