package cn.org.faster.framework.builder.common.config.datasource;

import cn.org.faster.framework.builder.common.config.web.context.MultipleDataSourceContext;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;

public class MultipleDataSource extends AbstractRoutingDataSource {

    protected DataSource determineTargetDataSource() {
        DataSource dataSource = MultipleDataSourceContext.getMultipleDataSourceThreadLocal();
        if (dataSource == null) {
            throw new IllegalStateException("Cannot determine target DataSource");
        }
        return dataSource;
    }

    @Override
    protected Object determineCurrentLookupKey() {
        return null;
    }
}