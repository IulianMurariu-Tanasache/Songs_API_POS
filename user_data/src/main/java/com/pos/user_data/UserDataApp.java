package com.pos.user_data;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.pos")
public class UserDataApp {
    public static void main(String[] args) {
        SpringApplication.run(UserDataApp.class, args);
    }
}
