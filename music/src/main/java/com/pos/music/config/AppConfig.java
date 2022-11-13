package com.pos.music.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public ModelMapper mapper() {
        ModelMapper mapper = new ModelMapper();
        return mapper;
    }
}
