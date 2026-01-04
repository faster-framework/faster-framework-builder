package cn.org.faster.framework.builder.common.model.request;

import lombok.Data;

@Data
public class DependencyRequest {
    /**
     * 版本
     */
    private String version;
    /**
     * 下载地址
     */
    private String url;
}
