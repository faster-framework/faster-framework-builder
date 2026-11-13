package cn.org.faster.framework.builder.common.net;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * 把官方骨架 ZIP 的内容并入生成结果。
 *
 * <p>相比原实现：不落临时文件、不持有未关闭的 {@code ZipFile}、对根目录文件（无子目录）安全跳过。</p>
 *
 * @author zhangbowen
 */
public final class FrameworkArchiveCopier {

    private FrameworkArchiveCopier() {
    }

    /**
     * @param skeletonZip   骨架 ZIP 内容
     * @param skipEntryNames 需要跳过（由生成器覆盖）的条目名，形如 {@code /pom.xml}
     * @param zipOutputStream 目标 ZIP 流
     * @throws IOException 写入失败
     */
    public static void copyInto(byte[] skeletonZip, Set<String> skipEntryNames, ZipOutputStream zipOutputStream)
            throws IOException {
        try (var zipInputStream = new ZipInputStream(new ByteArrayInputStream(skeletonZip), StandardCharsets.UTF_8)) {
            ZipEntry entry;
            while ((entry = zipInputStream.getNextEntry()) != null) {
                if (entry.isDirectory()) {
                    zipInputStream.closeEntry();
                    continue;
                }
                String name = entry.getName().replace('\\', '/');
                int firstSlash = name.indexOf('/');
                if (firstSlash < 0) {
                    // 骨架仓库根目录下的文件（README、LICENSE 等）不属于工程源码，跳过
                    zipInputStream.closeEntry();
                    continue;
                }
                String relative = name.substring(firstSlash);
                if (skipEntryNames.contains(relative)) {
                    zipInputStream.closeEntry();
                    continue;
                }
                zipOutputStream.putNextEntry(new ZipEntry(relative.substring(1)));
                zipInputStream.transferTo(zipOutputStream);
                zipOutputStream.closeEntry();
                zipInputStream.closeEntry();
            }
        }
    }
}
