package com.example.backend;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

// 测试文件
// 不要修改

@RestController
public class DemoController {

    @GetMapping("/hello")
    public String hello() {
        return "Hello, World!";
    }
}