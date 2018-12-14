package cn.org.faster.framework.builder.common.config.web.interceptor;

import cn.org.faster.framework.builder.common.config.web.context.MultipleDataSourceContext;
import org.springframework.beans.BeanUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

public class MultipleDataSourceInterceptor implements HandlerInterceptor {
    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response, Object handler, Exception ex) {
        MultipleDataSourceContext.clear();
    }
}
