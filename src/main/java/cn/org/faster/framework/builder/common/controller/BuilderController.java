package cn.org.faster.framework.builder.common.controller;

import cn.org.faster.framework.builder.common.config.ProjectOption;
import cn.org.faster.framework.builder.common.model.request.BuilderRequest;
import cn.org.faster.framework.builder.common.service.BuilderService;
import jakarta.validation.Valid;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * 代码生成入口。
 *
 * <p>响应契约统一为：成功返回 200 + ZIP 流，失败返回 JSON 错误体；
 * 不再出现"Service 写 201、Controller 返回 200"的冲突。</p>
 *
 * @author zhangbowen
 */
@RestController
@RequestMapping("/builder")
public class BuilderController {

    private static final MediaType APPLICATION_ZIP = MediaType.parseMediaType("application/zip");

    private final BuilderService builderService;

    public BuilderController(BuilderService builderService) {
        this.builderService = builderService;
    }

    /**
     * 生成类型选项（版本号与骨架地址以服务端配置为准）。
     *
     * @return 各类型可选项
     */
    @GetMapping("/options")
    public List<ProjectOption> options() {
        return builderService.options();
    }

    /**
     * 生成并下载项目压缩包。
     *
     * @param builderRequest 请求参数
     * @return ZIP 字节流
     */
    @PostMapping
    public ResponseEntity<byte[]> build(@Valid @RequestBody BuilderRequest builderRequest) {
        var archive = builderService.build(builderRequest);
        var disposition = ContentDisposition.attachment()
                .filename(archive.fileName(), StandardCharsets.UTF_8)
                .build();
        return ResponseEntity.ok()
                .contentType(APPLICATION_ZIP)
                .contentLength(archive.content().length)
                .header(HttpHeaders.CONTENT_DISPOSITION, disposition.toString())
                .body(archive.content());
    }
}
