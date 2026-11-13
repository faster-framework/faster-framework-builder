package cn.org.faster.framework.builder.common.compression;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 规范化 ZIP 条目名的输出流。
 *
 * <ol>
 *   <li>去掉前导 {@code /}（原先 {@code /src/main/java/...} 属于非标准绝对路径，部分解压工具会告警）；</li>
 *   <li>统一分隔符为 {@code /} 并折叠重复分隔符；</li>
 *   <li>同名条目只写入第一次，避免 {@code ZipException: duplicate entry}。</li>
 * </ol>
 *
 * <p>策略类依旧直接调用 {@code putNextEntry/write/closeEntry}，无需感知这些规则。</p>
 *
 * @author zhangbowen
 */
public class EntryNormalizingZipOutputStream extends ZipOutputStream {

    private final Set<String> writtenEntries = new HashSet<>();
    private boolean skippingEntry;

    public EntryNormalizingZipOutputStream(OutputStream out) {
        super(out);
    }

    @Override
    public void putNextEntry(ZipEntry entry) throws IOException {
        String name = normalize(entry.getName());
        if (name.isEmpty() || !writtenEntries.add(name)) {
            skippingEntry = true;
            return;
        }
        skippingEntry = false;
        ZipEntry normalized = new ZipEntry(name);
        if (entry.getTime() != -1) {
            normalized.setTime(entry.getTime());
        }
        super.putNextEntry(normalized);
    }

    @Override
    public void write(int b) throws IOException {
        if (!skippingEntry) {
            super.write(b);
        }
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        if (!skippingEntry) {
            super.write(b, off, len);
        }
    }

    @Override
    public void closeEntry() throws IOException {
        if (skippingEntry) {
            skippingEntry = false;
            return;
        }
        super.closeEntry();
    }

    /**
     * 规范化条目名。
     *
     * @param raw 原始名称
     * @return 去掉前导斜杠、折叠重复斜杠后的名称
     */
    public static String normalize(String raw) {
        if (raw == null) {
            return "";
        }
        String name = raw.replace('\\', '/');
        while (name.startsWith("/")) {
            name = name.substring(1);
        }
        while (name.contains("//")) {
            name = name.replace("//", "/");
        }
        return name;
    }
}
