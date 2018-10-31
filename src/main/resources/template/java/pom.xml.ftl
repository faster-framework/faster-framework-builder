<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>cn.org.faster</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>${dependencyVersion}</version>
    </parent>
    <groupId>${basePackagePath}</groupId>
    <artifactId>${projectName}</artifactId>
    <version>1.0-SNAPSHOT</version>
    <dependencies>
    <#list dependencies as item>
        <dependency>
            <groupId>${item.groupId}</groupId>
            <artifactId>${item.artifactId}</artifactId>
        </dependency>
    </#list>
    </dependencies>
    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
        <resources>
            <resource>
                <directory>src/main/java</directory>
                <includes>
                    <include>**/*.xml</include>
                </includes>
            </resource>
            <resource>
                <directory>src/main/resources</directory>
            </resource>
        </resources>
    </build>

</project>