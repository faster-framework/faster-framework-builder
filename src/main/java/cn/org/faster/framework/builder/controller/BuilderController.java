package cn.org.faster.framework.builder.controller;

import cn.org.faster.framework.builder.model.request.BuilderRequest;
import cn.org.faster.framework.builder.service.BuilderService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/builder")
@AllArgsConstructor
public class BuilderController {
    private BuilderService builderService;

    /**
     * 生成
     * @param builderRequest 请求参数
     * @return 压缩包
     */
    @PostMapping
    public ResponseEntity build(@RequestBody @Validated BuilderRequest builderRequest) throws IOException {
        builderService.build(builderRequest);
        return new ResponseEntity(HttpStatus.OK);
    }
}