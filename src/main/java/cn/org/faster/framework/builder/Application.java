package cn.org.faster.framework.builder;

import cn.org.faster.framework.builder.common.config.BuilderProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * Builder 应用入口。
 *
 * <p>运行时要求：JDK 17+、Spring Boot 4.x、Spring Framework 7.x。</p>
 *
 * @author zhangbowen
 */
@SpringBootApplication
@EnableConfigurationProperties(BuilderProperties.class)
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
