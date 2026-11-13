package cn.org.faster.framework.builder.common.context;

/**
 * 一次生成的产物：内存中的 ZIP 内容 + 建议的下载文件名。
 *
 * <p>不再落地临时文件，因此无需清理逻辑，也不会出现 {@code /tmp} 写满的问题。</p>
 *
 * @param content  ZIP 二进制内容
 * @param fileName 下载文件名（含 {@code .zip} 后缀）
 * @author zhangbowen
 */
public record GeneratedArchive(byte[] content, String fileName) {
}
