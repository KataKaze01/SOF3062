package com.example.demo.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

@CrossOrigin("*")
@RestController
public class MyRestApi {

    @GetMapping({"/poly/url0", "/"})
    public Object method0() {
        return Map.of("url", "/poly/url0", "message", "Public API - Ai cũng vào được");
    }

    @GetMapping("/poly/url1")
    public Object method1() {
        return Map.of("url", "/poly/url1", "message", "Authenticated - Phải đăng nhập mới vào được");
    }

    @GetMapping("/poly/url2")
    public Object method2() {
        return Map.of("url", "/poly/url2", "message", "Role USER - Chỉ USER vào được");
    }

    @GetMapping("/poly/url3")
    public Object method3() {
        return Map.of("url", "/poly/url3", "message", "Role ADMIN - Chỉ ADMIN vào được");
    }

    @GetMapping("/poly/url4")
    public Object method4() {
        return Map.of("url", "/poly/url4", "message", "Role USER hoặc ADMIN đều vào được");
    }
}