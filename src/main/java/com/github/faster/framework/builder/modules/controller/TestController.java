package com.github.faster.framework.builder.modules.controller;

import com.github.faster.framework.builder.modules.entity.Test;
import com.github.faster.framework.builder.modules.mapper.TestMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {
    @Autowired
    private TestMapper testMapper;

    @GetMapping
    public void test() {
        System.out.println(testMapper.selectAll());
    }

}