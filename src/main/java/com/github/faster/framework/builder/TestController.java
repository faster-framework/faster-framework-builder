package com.github.faster.framework.builder;

import com.github.faster.framework.builder.mapper.BusinessCollectMapper;
import com.github.faster.framework.core.web.context.SpringAppContextFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {
    @Autowired
    private BusinessCollectMapper businessCollectMapper;
    @Autowired
    private MultipleDataSource multipleDataSource;

    @GetMapping
    public void test() {
        System.out.println(businessCollectMapper.selectAll());
        multipleDataSource.clear();
    }

}