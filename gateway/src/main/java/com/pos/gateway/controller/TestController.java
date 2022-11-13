package com.pos.gateway.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/test")
    public String testSecurity() {
        return "works";
    }

    @GetMapping("/auth")
    public String testAuthorities() {
        return "this works even better";
    }
}
