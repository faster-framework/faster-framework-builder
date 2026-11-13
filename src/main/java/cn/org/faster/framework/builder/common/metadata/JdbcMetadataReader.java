package cn.org.faster.framework.builder.common.metadata;

import cn.org.faster.framework.builder.common.config.BuilderProperties;
import cn.org.faster.framework.builder.common.exception.BuilderException;
import cn.org.faster.framework.builder.common.model.ColumnModel;
import cn.org.faster.framework.builder.common.model.TableColumnModel;
import cn.org.faster.framework.builder.common.model.request.DatabaseRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * 通过纯 JDBC 读取 {@code information_schema}。
 *
 * <p>替代了原先 MyBatis-Plus + Druid + {@code AbstractRoutingDataSource} + ThreadLocal 的组合：
 * 连接在本次调用内开启并关闭，不存在跨请求泄漏、残留或线程复用串数据的问题。</p>
 *
 * @author zhangbowen
 */
@Component
public class JdbcMetadataReader implements MetadataReader {

    private static final Logger log = LoggerFactory.getLogger(JdbcMetadataReader.class);

    private static final String BASE_SQL = """
            SELECT t.TABLE_SCHEMA,
                   t.TABLE_NAME,
                   t.TABLE_COMMENT,
                   c.COLUMN_NAME,
                   c.DATA_TYPE,
                   c.ORDINAL_POSITION,
                   c.COLUMN_COMMENT,
                   c.IS_NULLABLE
            FROM INFORMATION_SCHEMA.TABLES t
            JOIN INFORMATION_SCHEMA.COLUMNS c
              ON t.TABLE_SCHEMA = c.TABLE_SCHEMA
             AND t.TABLE_NAME = c.TABLE_NAME
            WHERE t.TABLE_SCHEMA = ?
              AND t.TABLE_TYPE = 'BASE TABLE'
              AND t.TABLE_NAME NOT LIKE 'sys\\_%'
            """;

    private final BuilderProperties properties;

    public JdbcMetadataReader(BuilderProperties properties) {
        this.properties = properties;
    }

    @Override
    public List<TableColumnModel> read(DatabaseRequest database, String tableName) {
        boolean singleTable = tableName != null && !tableName.isBlank();
        String sql = singleTable ? BASE_SQL + " AND t.TABLE_NAME = ?\n" : BASE_SQL;
        String url = JdbcEndpoint.buildUrl(database, properties);

        long start = System.currentTimeMillis();
        try (Connection connection = openConnection(url, database);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setQueryTimeout((int) Math.max(1, properties.queryTimeout().toSeconds()));
            statement.setString(1, database.name());
            if (singleTable) {
                statement.setString(2, tableName);
            }
            Map<String, TableColumnModel> tables = new LinkedHashMap<>();
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    TableColumnModel table = tables.computeIfAbsent(
                            resultSet.getString("TABLE_SCHEMA") + "." + resultSet.getString("TABLE_NAME"),
                            key -> newTable(resultSet));
                    table.getColumnList().add(newColumn(resultSet));
                }
            }
            log.info("读取元数据完成: schema={}, table={}, 表数={}, 耗时={}ms",
                    database.name(), singleTable ? tableName : "*", tables.size(), System.currentTimeMillis() - start);
            List<TableColumnModel> result = new ArrayList<>(tables.values());
            if (singleTable && result.isEmpty()) {
                throw new BuilderException(HttpStatus.NOT_FOUND,
                        "库 " + database.name() + " 中不存在表 " + tableName);
            }
            return result;
        } catch (SQLException ex) {
            throw new BuilderException(HttpStatus.BAD_GATEWAY,
                    "无法读取数据库元数据，请检查连接信息：" + ex.getMessage(), ex);
        }
    }

    private Connection openConnection(String url, DatabaseRequest database) throws SQLException {
        Properties connectionProperties = new Properties();
        connectionProperties.setProperty("user", database.username() == null ? "" : database.username());
        connectionProperties.setProperty("password", database.password() == null ? "" : database.password());
        return DriverManager.getConnection(url, connectionProperties);
    }

    private static TableColumnModel newTable(ResultSet resultSet) {
        TableColumnModel table = new TableColumnModel();
        try {
            table.setTableSchema(resultSet.getString("TABLE_SCHEMA"));
            table.setTableName(resultSet.getString("TABLE_NAME"));
            table.setTableComment(resultSet.getString("TABLE_COMMENT"));
        } catch (SQLException ex) {
            throw new BuilderException(HttpStatus.BAD_GATEWAY, "读取表信息失败", ex);
        }
        return table;
    }

    private static ColumnModel newColumn(ResultSet resultSet) {
        ColumnModel column = new ColumnModel();
        try {
            column.setColumnName(resultSet.getString("COLUMN_NAME"));
            column.setDataType(resultSet.getString("DATA_TYPE"));
            column.setOrdinalPosition(resultSet.getInt("ORDINAL_POSITION"));
            column.setColumnComment(resultSet.getString("COLUMN_COMMENT"));
            column.setIsNullable(resultSet.getString("IS_NULLABLE"));
        } catch (SQLException ex) {
            throw new BuilderException(HttpStatus.BAD_GATEWAY, "读取列信息失败", ex);
        }
        return column;
    }
}
