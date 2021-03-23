package com.xfggh.blog.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @Value("${test.hello:默认值}")
    private String testHello;

    @RequestMapping("/hello")
    public String hello(){
        return "hello world//" + testHello;
    }
}
