package cn.org.faster.framework.builder.common.metadata;

import cn.org.faster.framework.builder.common.model.TableColumnModel;
import cn.org.faster.framework.builder.common.model.request.DatabaseRequest;

import java.util.List;

/**
 * 表元数据读取器。
 *
 * <p>抽象出来是为了让生成流程可以脱离真实 MySQL 做端到端测试。</p>
 *
 * @author zhangbowen
 */
public interface MetadataReader {

    /**
     * 读取指定库下的表与列信息。
     *
     * @param database  数据库连接信息
     * @param tableName 表名，为空时读取整库
     * @return 表及其列，按表名、列序号排序
     */
    List<TableColumnModel> read(DatabaseRequest database, String tableName);
}
