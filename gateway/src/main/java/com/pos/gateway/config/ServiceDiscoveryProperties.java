package com.pos.gateway.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("service")
public class ServiceDiscoveryProperties {
    private String idmServiceLocation;
    private String songServiceLocation;
    private String artistServiceLocation;
    private String playlistServiceLocation;
    private String userDataServiceLocation;
    private String frontEndLocation;
}
