package com.github.faster.framework.builder.model.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

/**
 * 数据库配置请求
 */
@Data
public class DatabaseRequest {
    /**
     * 数据库地址
     */
    @NotBlank
    private String host;
    /**
     * 端口
     */
    @NotBlank
    private String port;
    /**
     * 数据库名称
     */
    @NotBlank
    private String name;
    /**
     * 账号
     */
    @NotBlank
    private String username;
    /**
     * 密码
     */
    @NotBlank
    private String password;
}
