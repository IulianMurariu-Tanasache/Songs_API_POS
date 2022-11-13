package com.pos.music;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SongsServiceApp {

    //TODO: make albums have songs -> create a join table between albums and songs
    public static void main(String[] args) {
        SpringApplication.run(SongsServiceApp.class, args);
    }
}
