package com.github.faster.framework.builder.common.datasource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class MultipleDataSourceConfiguration {
    @Bean
    public MultipleDataSource multipleDataSource() {
        MultipleDataSource multipleDataSource = new MultipleDataSource();
        Map<Object, Object> map = new HashMap<>();
        multipleDataSource.setTargetDataSources(map);
        return multipleDataSource;
    }
}
