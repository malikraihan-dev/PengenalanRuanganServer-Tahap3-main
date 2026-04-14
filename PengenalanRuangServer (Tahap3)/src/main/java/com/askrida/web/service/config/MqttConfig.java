package com.askrida.web.service.config;

import org.springframework.context.annotation.Configuration;

/**
 * MQTT Configuration - temporarily disabled for compilation
 * Enable after fixing Spring Integration MQTT dependencies  
 */
@Configuration
public class MqttConfig {
    
    public boolean isBrokerEnabled() {
        return false;
    }
}
