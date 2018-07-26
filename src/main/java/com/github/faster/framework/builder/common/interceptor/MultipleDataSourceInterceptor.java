package com.github.faster.framework.builder.common.interceptor;

import com.github.faster.framework.builder.common.context.MultipleDataSourceContext;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MultipleDataSourceInterceptor implements HandlerInterceptor {
    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response, Object handler, Exception ex) {
        MultipleDataSourceContext.clear();
    }
}
