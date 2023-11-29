package ru.softplat.main.server.web.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.softplat.stats.dto.StatsCreateDto;

import java.util.List;
import java.util.Map;

@Service
public class StatClientt {
    private static final String API_PREFIX = "/stats";
    private final RestTemplate rest;

    @Autowired
    public StatClientt(@Value("${ssai-main.url}") String serverUrl, RestTemplateBuilder builder) {
        this.rest = builder
                .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                .build();
    }

    public void postStat(StatsCreateDto statsCreateDto) {
        makeAndSendRequest(HttpMethod.POST, "", null, statsCreateDto);
    }

    private <T> void makeAndSendRequest(HttpMethod method, String path,
                                                          @Nullable Map<String, Object> parameters, @Nullable T body) {
        HttpEntity<T> requestEntity = new HttpEntity<>(body, defaultHeaders());

        try {
            if (parameters != null)
                rest.exchange(path, method, requestEntity, Object.class, parameters);
            else rest.exchange(path, method, requestEntity, Object.class);
        } catch (HttpStatusCodeException ignored) {

        }
    }

    private HttpHeaders defaultHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        return headers;
    }
}
