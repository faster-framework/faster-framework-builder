package com.github.faster.framework.builder.common.web;

import com.github.faster.framework.builder.common.interceptor.MultipleDataSourceInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class BuilderWebConfiguration implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new MultipleDataSourceInterceptor()).addPathPatterns("/**");
    }
}
