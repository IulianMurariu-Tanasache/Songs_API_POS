package com.pos.gateway.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@EnableConfigurationProperties({ServiceDiscoveryProperties.class, EnvelopeProperties.class})
public class AppConfig {

    @Bean
    public WebClient webClient() {
        return WebClient.create();
    }
}
