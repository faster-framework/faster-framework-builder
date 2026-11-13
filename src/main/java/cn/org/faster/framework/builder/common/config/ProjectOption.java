package cn.org.faster.framework.builder.common.config;

/**
 * 一种生成类型对应的框架版本与官方骨架下载地址。
 *
 * <p>放在后端配置里，前端不再硬编码版本号，避免"页面显示 1.3.8、实际下载 1.0.3"这类漂移。</p>
 *
 * @param type    生成类型：1 接口、2 管理端接口、3 管理端前端、4 合一、5 SQL、6 CMS 接口、7 CMS 前端
 * @param version 生成的 pom 中使用的框架版本
 * @param url     官方骨架 ZIP 地址，为空表示该类型不使用骨架
 * @author zhangbowen
 */
public record ProjectOption(int type, String version, String url) {
}
