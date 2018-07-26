package com.github.faster.framework.builder;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public MultipleDataSource multipleDataSource() {
        MultipleDataSource multipleDataSource = new MultipleDataSource();
        Map<Object, Object> map = new HashMap<>();
        multipleDataSource.setTargetDataSources(map);
        return multipleDataSource;
    }
}
