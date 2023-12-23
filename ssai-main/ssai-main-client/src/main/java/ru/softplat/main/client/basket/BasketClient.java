package ru.softplat.main.client.basket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.softplat.main.client.BaseClient;
import ru.softplat.main.dto.basket.BasketCreateDto;

import java.util.Map;

@Service
public class BasketClient extends BaseClient {
    private static final String API_PREFIX = "/basket";

    @Autowired
    public BasketClient(@Value("${main-server.url:http://localhost:8080}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .build()
        );
    }

    public ResponseEntity<Object> addProductInBasket(long userId, long productId, boolean installation) {
        Map<String, Object> parameters = Map.of(
                "installation", installation
        );
        return post("/" + productId + "?installation={installation}", userId, parameters);
    }

    public ResponseEntity<Object> saveBasket(long userId, BasketCreateDto basketCreateDto) {
        return post("", userId, basketCreateDto);
    }

    public ResponseEntity<Object> removeProductFromBasket(long userId, long productId, boolean installation) {
        Map<String, Object> parameters = Map.of(
                "installation", installation
        );
        return delete("/product/" + productId + "?installation={installation}", userId, parameters);
    }

    public ResponseEntity<Object> getBasket(long userId) {
        return get("", userId);
    }

    public void clearBasket(long userId) {
        delete("", userId);
    }

    public ResponseEntity<Object> removeBasketPosition(long userId, long basketPositionId) {
        return delete("/" + basketPositionId, userId);
    }
}