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
    @NotBlank(message = "数据库主机名不能为空")
    private String host;
    /**
     * 端口
     */
    @NotBlank(message = "数据库端口号不能为空")
    @Pattern(message = "端口号格式错误", regexp = "\\d+")
    private String port;
    /**
     * 数据库名称
     */
    @NotBlank(message = "数据库名称不能为空")
    private String name;
    /**
     * 账号
     */
    @NotBlank(message = "数据库账号不能为空")
    private String username;
    /**
     * 密码
     */
    @NotBlank(message = "数据库密码不能为空")
    private String password;
}
