package com.github.faster.framework.builder;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.util.Assert;

import javax.sql.DataSource;

public class MultipleDataSource extends AbstractRoutingDataSource {
    private static DruidDataSource dataSource;

    protected DataSource determineTargetDataSource() {
        System.out.println("12345");
        dataSource = new DruidDataSource();
        dataSource.setUrl("jdbc:mysql://139.199.23.31:3306/faster-admin?allowMultiQueries=true&useSSL=true");
        dataSource.setUsername("root");
        dataSource.setPassword("18764050615");
        return dataSource;
    }

    @Override
    protected Object determineCurrentLookupKey() {
        return null;
    }

    public static void clear() {
        dataSource.close();
    }
}