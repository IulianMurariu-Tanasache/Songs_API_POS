package com.pos.gateway.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("service")
public class ServiceDiscoveryProperties {
    private String idmHost;
    private int idmPort;
    /*private String artistHost;
    private int artistPort;
    private String musicHost;
    private int musicPort;*/
}
