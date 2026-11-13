package cn.org.faster.framework.builder;

import cn.org.faster.framework.builder.common.metadata.MetadataReader;
import cn.org.faster.framework.builder.common.model.ColumnModel;
import cn.org.faster.framework.builder.common.model.TableColumnModel;
import cn.org.faster.framework.builder.common.model.request.DatabaseRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("POST /builder 端到端")
class BuilderApiTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MetadataReader metadataReader;

    @Test
    @DisplayName("选项接口下发版本与骨架地址")
    void options() throws Exception {
        mockMvc.perform(get("/builder/options"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(7))
                .andExpect(jsonPath("$[0].type").value(1))
                .andExpect(jsonPath("$[1].version").isNotEmpty());
    }

    @Test
    @DisplayName("type=5 无需数据库即可生成 SQL 包")
    void generateSqlWithoutDatabase() throws Exception {
        var result = mockMvc.perform(post("/builder")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"type":5,"business":{"projectName":"demo"},"database":null,"dependency":null}
                                """))
                .andExpect(status().isOk())
                .andReturn();

        String disposition = result.getResponse().getHeader("Content-Disposition");
        assertThat(disposition).contains("attachment").contains("filename*=UTF-8''demo.zip");

        Map<String, String> entries = entries(result.getResponse().getContentAsByteArray());
        assertThat(entries).containsKeys("base.sql", "demo.sql");
        assertThat(entries.get("base.sql")).contains("CREATE TABLE `sys_permission`");
    }

    @Test
    @DisplayName("type=1 生成完整工程且条目名不含前导斜杠")
    void generateApiProject() throws Exception {
        when(metadataReader.read(any(DatabaseRequest.class), eq(null))).thenReturn(List.of(userTable()));

        var result = mockMvc.perform(post("/builder")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "type": 1,
                                  "database": {"host":"127.0.0.1","port":"3306","name":"demo","username":"root","password":"root"},
                                  "business": {"projectName":"demo-api","basePackagePath":"com.demo","tableName":null},
                                  "dependency": {"version":"1.4.2.RELEASE","url":null}
                                }
                                """))
                .andExpect(status().isOk())
                .andReturn();

        Map<String, String> entries = entries(result.getResponse().getContentAsByteArray());
        assertThat(entries.keySet())
                .anyMatch(name -> name.contains("src/main/java/com/demo/modules/v1/user/entity/User.java"))
                .anyMatch(name -> name.contains("src/main/java/com/demo/modules/v1/user/controller/"))
                .anyMatch(name -> name.equals("pom.xml"))
                .noneMatch(name -> name.startsWith("/"));

        String entity = entries.entrySet().stream()
                .filter(entry -> entry.getKey().endsWith("User.java"))
                .findFirst()
                .orElseThrow()
                .getValue();
        assertThat(entity).contains("public class User extends BaseEntity", "private String userName;");
    }

    @Test
    @DisplayName("缺少业务信息返回 400 与可读错误")
    void rejectMissingBusiness() throws Exception {
        mockMvc.perform(post("/builder")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"type":1,"business":null,"database":null,"dependency":null}
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").isNotEmpty());
    }

    @Test
    @DisplayName("非法类型返回 400")
    void rejectInvalidType() throws Exception {
        mockMvc.perform(post("/builder")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"type":99,"business":{"projectName":"demo"},"database":null,"dependency":null}
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").isNotEmpty());
    }

    @Test
    @DisplayName("需要数据库的类型未提供连接信息时返回 400")
    void rejectMissingDatabase() throws Exception {
        mockMvc.perform(post("/builder")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"type":1,"business":{"projectName":"demo","basePackagePath":"com.demo"},"database":null,"dependency":null}
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("当前生成类型需要数据库连接信息"));
    }

    private static TableColumnModel userTable() {
        TableColumnModel table = new TableColumnModel();
        table.setTableName("tb_user");
        table.setTableSchema("demo");
        table.setTableComment("用户表");
        table.setColumnList(List.of(column("user_name", "varchar", "用户名"), column("age", "int", "年龄")));
        return table;
    }

    private static ColumnModel column(String name, String dataType, String comment) {
        ColumnModel column = new ColumnModel();
        column.setColumnName(name);
        column.setDataType(dataType);
        column.setOrdinalPosition(1);
        column.setColumnComment(comment);
        column.setIsNullable("YES");
        return column;
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
