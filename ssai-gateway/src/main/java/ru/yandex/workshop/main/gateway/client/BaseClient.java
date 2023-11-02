package ru.yandex.workshop.main.gateway.client;

import org.springframework.http.*;
import org.springframework.lang.Nullable;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

public class BaseClient {
    protected final RestTemplate rest;

    public BaseClient(RestTemplate rest) {
        this.rest = rest;
    }

    protected ResponseEntity<Object> get(String path, String email) {
        return get(path, email, null);
    }

    protected ResponseEntity<Object> get(String path, String email, @Nullable Map<String, Object> parameters) {
        return makeAndSendRequest(HttpMethod.GET, path, email, parameters, null);
    }

    protected <T> ResponseEntity<Object> post(String path, T body) {
        return post(path, null, null, body);
    }

    protected <T> ResponseEntity<Object> post(String path, String email, T body) {
        return post(path, email, null, body);
    }

    protected <T> ResponseEntity<Object> post(String path, String email, @Nullable Map<String, Object> parameters, T body) {
        return makeAndSendRequest(HttpMethod.POST, path, email, parameters, body);
    }

    protected <T> ResponseEntity<Object> put(String path, String email, T body) {
        return put(path, email, null, body);
    }

    protected <T> ResponseEntity<Object> put(String path, String email, @Nullable Map<String, Object> parameters, T body) {
        return makeAndSendRequest(HttpMethod.PUT, path, email, parameters, body);
    }

    protected <T> ResponseEntity<Object> patch(String path, String email) {
        return patch(path, email, null, null);
    }

    protected <T> ResponseEntity<Object> patch(String path, String email, T body) {
        return patch(path, email, null, body);
    }

    protected <T> ResponseEntity<Object> patch(String path, String email, @Nullable Map<String, Object> parameters, T body) {
        return makeAndSendRequest(HttpMethod.PATCH, path, email, parameters, body);
    }

    protected <T> ResponseEntity<Object> delete(String path, String email) {
        return delete(path, email, null);
    }

    protected <T> ResponseEntity<Object> delete(String path, String email, @Nullable Map<String, Object> parameters) {
        return makeAndSendRequest(HttpMethod.DELETE, path, email, parameters, null);
    }

    private <T> ResponseEntity<Object> makeAndSendRequest(HttpMethod method, String path, String email, @Nullable Map<String, Object> parameters, @Nullable T body) {
        HttpEntity<T> requestEntity = new HttpEntity<>(body, defaultHeaders(email));

        ResponseEntity<Object> ssaiServerResponse;
        try {
            if (parameters != null) {
                ssaiServerResponse = rest.exchange(path, method, requestEntity, Object.class, parameters);
            } else {
                ssaiServerResponse = rest.exchange(path, method, requestEntity, Object.class);
            }
        } catch (HttpStatusCodeException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsByteArray());
        }
        return prepareGatewayResponse(ssaiServerResponse);
    }

    private HttpHeaders defaultHeaders(String email) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        if (email != null) {
            headers.set("X-Sharer-User-Email", email);
        }
        return headers;
    }

    private static ResponseEntity<Object> prepareGatewayResponse(ResponseEntity<Object> response) {
        if (response.getStatusCode().is2xxSuccessful()) {
            return response;
        }

        ResponseEntity.BodyBuilder responseBuilder = ResponseEntity.status(response.getStatusCode());

        if (response.hasBody()) {
            return responseBuilder.body(response.getBody());
        }

        return responseBuilder.build();
    }
}