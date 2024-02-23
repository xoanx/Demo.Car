package com.example.demo.map;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HeremapConfig {
    @Value("${here.maps.apiKey}")
    private String apiKey;
    @Bean
    public HeremapService heremapService() {
        return new HeremapService(apiKey);
    }
}

