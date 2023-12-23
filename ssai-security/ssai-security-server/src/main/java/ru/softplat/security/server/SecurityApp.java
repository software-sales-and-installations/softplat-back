package ru.softplat.security.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = {"ru.softplat.stats.client", "ru.softplat.main.client",
        "ru.softplat.security"})
@SpringBootApplication
public class SecurityApp {
    public static void main(String[] args) {
        SpringApplication.run(SecurityApp.class, args);
    }
}