package cn.org.faster.framework.builder.common.compression;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("ZIP 条目规范化")
class EntryNormalizingZipOutputStreamTest {

    @Test
    @DisplayName("去掉前导斜杠、反斜杠转正斜杠")
    void normalizeNames() throws Exception {
        byte[] zip = zipOf(Map.of("/src/main/java/A.java", "class A {}"));

        assertThat(entries(zip)).containsOnlyKeys("src/main/java/A.java");
    }

    @Test
    @DisplayName("重复条目只保留第一次的内容，不抛 ZipException")
    void skipDuplicateEntries() throws Exception {
        Map<String, String> files = new LinkedHashMap<>();
        files.put("/a.txt", "first");
        files.put("a.txt", "second");

        byte[] zip = zipOf(files);
        Map<String, String> entries = entries(zip);

        assertThat(entries).containsOnlyKeys("a.txt");
        assertThat(entries.get("a.txt")).isEqualTo("first");
    }

    @Test
    @DisplayName("normalize 处理空值与重复分隔符")
    void normalizeStatic() {
        assertThat(EntryNormalizingZipOutputStream.normalize(null)).isEmpty();
        assertThat(EntryNormalizingZipOutputStream.normalize("//a//b\\\\c")).isEqualTo("a/b/c");
    }

    private static byte[] zipOf(Map<String, String> files) throws Exception {
        var buffer = new ByteArrayOutputStream();
        try (var zip = new EntryNormalizingZipOutputStream(buffer)) {
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
        try (var input = new ZipInputStream(new ByteArrayInputStream(zip))) {
            ZipEntry entry;
            while ((entry = input.getNextEntry()) != null) {
                result.put(entry.getName(), new String(input.readAllBytes(), StandardCharsets.UTF_8));
            }
        }
        return result;
    }
}
