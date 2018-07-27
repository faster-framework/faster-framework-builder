package com.github.faster.framework.builder.constants;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum BuilderTypeEnum {
    API(1),//接口项目
    ADMIN_API(2),//admin接口项目
    ADMIN_WEB(3);//admin-web项目

    private final Integer type;
}
