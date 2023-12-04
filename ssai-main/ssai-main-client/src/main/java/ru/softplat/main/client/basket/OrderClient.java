package ru.softplat.main.client.basket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.softplat.main.client.BaseClient;
import ru.softplat.main.dto.basket.OrderCreateDto;

@Service
public class OrderClient extends BaseClient {
    private static final String API_PREFIX = "/orders";

    @Autowired
    public OrderClient(@Value("${main-server.url:http://localhost:8080}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .build()
        );
    }

    public ResponseEntity<Object> addOrder(long userId, OrderCreateDto orderCreateDto) {
        return post("", userId, orderCreateDto);
    }

    public ResponseEntity<Object> getOrder(long userId, long orderId) {
        return get("/" + orderId, userId);
    }

    public ResponseEntity<Object> getAllOrders(long userId) {
        return get("", userId);
    }
}