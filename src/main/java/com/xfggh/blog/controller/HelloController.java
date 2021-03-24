package com.xfggh.blog.controller;

import com.xfggh.blog.entity.Test;
import com.xfggh.blog.service.TestService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
public class HelloController {

    @Resource
    private TestService testService;

    @Value("${test.hello:默认值}")
    private String testHello;

    @RequestMapping("/hello")
    public String hello(){
        return "hello world//" + testHello;
    }

    @GetMapping("/test/list")
    public List<Test> list() {
        return testService.list();
    }
}
