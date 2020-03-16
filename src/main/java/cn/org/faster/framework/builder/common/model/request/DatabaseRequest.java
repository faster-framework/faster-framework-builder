package cn.org.faster.framework.builder.common.model.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * 数据库配置请求
 */
@Data
public class DatabaseRequest {
    /**
     * 数据库地址
     */
    private String host;
    /**
     * 端口
     */
    private String port;
    /**
     * 数据库名称
     */
    private String name;
    /**
     * 账号
     */
    private String username;
    /**
     * 密码
     */
    private String password;
}
