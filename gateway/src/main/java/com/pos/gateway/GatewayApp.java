package com.pos.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(scanBasePackages = "com.pos", exclude = {SecurityAutoConfiguration.class })
public class GatewayApp {

    public static void main(String[] args) {
        SpringApplication.run(GatewayApp.class,args);
    }
}
