package cn.org.faster.framework.builder.common.config.web.context;

import com.alibaba.druid.pool.DruidDataSource;
import cn.org.faster.framework.builder.common.model.request.DatabaseRequest;

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
        dataSource.setBreakAfterAcquireFailure(true);
        dataSource.setTestWhileIdle(true);
        dataSource.setTimeBetweenConnectErrorMillis(5000);
        dataSource.setValidationQuery("select 1");
        dataSource.setMaxWait(5000);
        dataSource.setMaxActive(1);
        dataSource.setMaxWaitThreadCount(1);
        multipleDataSourceThreadLocal.set(dataSource);
    }

    public static void clear() {
        DruidDataSource druidDataSource = multipleDataSourceThreadLocal.get();
        if (druidDataSource != null) {
            druidDataSource.close();
        }
        multipleDataSourceThreadLocal.remove();
    }
}
