package ru.softplat.stats.server.util;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public ApachePOI apachePOI() {
        return new ApachePOI();
    }
}