package ru.softplat.stats.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = {"ru.softplat.main.client", "ru.softplat.stats"})
@SpringBootApplication
public class StatsApp {
    public static void main(String[] args) {
        SpringApplication.run(StatsApp.class, args);
    }
}