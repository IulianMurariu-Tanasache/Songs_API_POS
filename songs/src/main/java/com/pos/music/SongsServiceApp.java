package com.pos.music;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@EntityScan(basePackages = "com.pos.commons.entity")
@SpringBootApplication(scanBasePackages = "com.pos")
public class SongsServiceApp {

    //TODO: make albums have songs -> create a join table between albums and songs
    public static void main(String[] args) {
        SpringApplication.run(SongsServiceApp.class, args);
    }
}
