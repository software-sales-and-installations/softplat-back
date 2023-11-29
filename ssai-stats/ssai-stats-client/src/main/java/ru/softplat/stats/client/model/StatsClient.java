package ru.softplat.stats.client.model;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.softplat.stats.client.client.BaseClient;
import ru.softplat.stats.dto.StatsCreateDto;

@Service
@Slf4j
public class StatsClient extends BaseClient {
    private static final String API_PREFIX = "/stats";

    @Autowired
    public StatsClient(@Value("${stats-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .build()
        );
    }

    public void addStats(StatsCreateDto statsCreateDto) {
        post("", statsCreateDto);
    }
}