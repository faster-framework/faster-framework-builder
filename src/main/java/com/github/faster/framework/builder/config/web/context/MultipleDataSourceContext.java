package com.github.faster.framework.builder.config.web.context;

import com.alibaba.druid.pool.DruidDataSource;
import com.github.faster.framework.builder.model.request.DatabaseRequest;

public class MultipleDataSourceContext {
    private static ThreadLocal<DruidDataSource> multipleDataSourceThreadLocal = new ThreadLocal<>();

    public static DruidDataSource getMultipleDataSourceThreadLocal() {
        return multipleDataSourceThreadLocal.get();
    }

    public static void setMultipleDataSourceThreadLocal(DatabaseRequest databaseRequest) {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUrl("jdbc:mysql://" + databaseRequest.getHost() + ":" + databaseRequest.getPort() + "/information_schema?allowMultiQueries=true&useSSL=false");
        dataSource.setUsername(databaseRequest.getUsername());
        dataSource.setPassword(databaseRequest.getPassword());
        multipleDataSourceThreadLocal.set(dataSource);
    }

    public static void clear() {
        multipleDataSourceThreadLocal.get().close();
        multipleDataSourceThreadLocal.remove();
    }
}
