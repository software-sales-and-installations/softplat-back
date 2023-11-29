package ru.softplat.main.client.model;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.softplat.main.client.client.BaseClient;
import ru.softplat.security.dto.UserCreateMainDto;


import java.util.Objects;

@Service
@Slf4j
public class MainClient extends BaseClient {
    private static final Gson gson =  new Gson();

    @Value("${stats-server.url}")
    private String serverUrl;

    @Autowired
    public MainClient(RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory())
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public void createUser(HttpEntity<Object> request) {
        UserCreateMainDto dataRequest = gson.fromJson(Objects.requireNonNull(request.getBody()).toString(), UserCreateMainDto.class);
        UserCreateMainDto requestDto = UserCreateMainDto.builder()
                .email(dataRequest.getEmail())
                .name(dataRequest.getName())
                .phone(dataRequest.getPhone())
                .status(dataRequest.getStatus())
                .role(dataRequest.getRole())
                .build();
        post(serverUrl + "/product/stats", requestDto);
    }



}