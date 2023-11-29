package ru.softplat.stats.client.model;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.softplat.stats.client.client.BaseClient;
import ru.softplat.stats.dto.StatsCreateDto;

import java.util.Objects;

@Service
@Slf4j
public class StatsClient extends BaseClient {
    private static final Gson gson =  new Gson();

    @Value("${stats-server.url}")
    private String serverUrl;

    @Autowired
    public StatsClient(RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory())
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public void addStats(HttpEntity<Object> request) {
        StatsCreateDto dataRequest = gson.fromJson(Objects.requireNonNull(request.getBody()).toString(), StatsCreateDto.class);
        StatsCreateDto requestDto = StatsCreateDto.builder()
                .buyer(dataRequest.getBuyer())
                .product(dataRequest.getProduct())
                .dateBuy(dataRequest.getDateBuy())
                .quantity(dataRequest.getQuantity())
                .amount(dataRequest.getAmount())
                .build();
        post(serverUrl + "/product/stats", requestDto);
    }

}