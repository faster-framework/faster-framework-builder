package cn.org.faster.framework.builder.common.net;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("官方骨架合并")
class FrameworkArchiveCopierTest {

    @Test
    @DisplayName("去掉仓库目录前缀，跳过指定条目与根目录文件")
    void copyInto() throws Exception {
        byte[] skeleton = skeletonZip();
        var buffer = new ByteArrayOutputStream();

        try (ZipOutputStream out = new ZipOutputStream(buffer)) {
            FrameworkArchiveCopier.copyInto(skeleton, Set.of("/pom.xml"), out);
        }

        Map<String, String> entries = entries(buffer.toByteArray());
        assertThat(entries).containsOnlyKeys("src/Main.java", "README.md");
        assertThat(entries.get("src/Main.java")).isEqualTo("class Main {}");
    }

    private static byte[] skeletonZip() throws Exception {
        Map<String, String> files = new LinkedHashMap<>();
        files.put("repo-master/pom.xml", "<project/>");
        files.put("repo-master/src/Main.java", "class Main {}");
        files.put("README.md", "root file");
        files.put("repo-master/README.md", "README");
        var buffer = new ByteArrayOutputStream();
        try (ZipOutputStream zip = new ZipOutputStream(buffer)) {
            zip.putNextEntry(new ZipEntry("repo-master/"));
            zip.closeEntry();
            for (Map.Entry<String, String> file : files.entrySet()) {
                zip.putNextEntry(new ZipEntry(file.getKey()));
                zip.write(file.getValue().getBytes(StandardCharsets.UTF_8));
                zip.closeEntry();
            }
        }
        return buffer.toByteArray();
    }

    private static Map<String, String> entries(byte[] zip) throws Exception {
        Map<String, String> result = new LinkedHashMap<>();
        try (ZipInputStream input = new ZipInputStream(new ByteArrayInputStream(zip))) {
            ZipEntry entry;
            while ((entry = input.getNextEntry()) != null) {
                result.put(entry.getName(), new String(input.readAllBytes(), StandardCharsets.UTF_8));
            }
        }
        return result;
    }
}
