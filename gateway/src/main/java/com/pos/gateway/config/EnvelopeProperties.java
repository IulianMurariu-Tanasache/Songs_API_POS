package com.pos.gateway.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("envelope")
public class EnvelopeProperties {
    private String noBodyEnvelope;
}
