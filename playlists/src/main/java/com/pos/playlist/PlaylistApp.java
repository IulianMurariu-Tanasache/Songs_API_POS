package com.pos.playlist;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.pos")
public class PlaylistApp {
    public static void main(String[] args) {
        SpringApplication.run(PlaylistApp.class, args);
    }
}
