package ru.softplat.stats.server;

import lombok.SneakyThrows;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class StatsApp {
    @SneakyThrows
    public static void main(String[] args) {
        SpringApplication.run(StatsApp.class, args);
//        ApachePOI apachePOI = new ApachePOI();
//        apachePOI.createFile();
    }
}