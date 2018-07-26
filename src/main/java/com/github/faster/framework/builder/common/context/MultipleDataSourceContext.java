package com.github.faster.framework.builder.common.context;

import com.alibaba.druid.pool.DruidDataSource;

public class MultipleDataSourceContext {
    private static ThreadLocal<DruidDataSource> multipleDataSourceThreadLocal = new ThreadLocal<>();

    public static DruidDataSource getMultipleDataSourceThreadLocal() {
        return multipleDataSourceThreadLocal.get();
    }

    public static void setMultipleDataSourceThreadLocal(DruidDataSource dataSource) {
        multipleDataSourceThreadLocal.set(dataSource);
    }

    public static void clear() {
        multipleDataSourceThreadLocal.get().close();
        multipleDataSourceThreadLocal.remove();
    }
}
