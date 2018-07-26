package com.github.faster.framework.builder;


import lombok.Data;
import lombok.ToString;

import javax.persistence.Table;

@Table(name = "sys_user")
@Data
public class Test {
    private String name;
}
