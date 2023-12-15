package ru.softplat.main.client.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.softplat.main.client.BaseClient;
import ru.softplat.main.dto.user.BuyerUpdateDto;
import ru.softplat.security.dto.UserCreateMainDto;

import java.util.Map;

@Service
public class BuyerClient extends BaseClient {
    private static final String API_PREFIX = "/buyer";

    @Autowired
    public BuyerClient(@Value("${main-server.url:http://localhost:8080}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .build()
        );
    }

    public ResponseEntity<Object> createBuyer(UserCreateMainDto userCreateMainDto) {
        return post("", userCreateMainDto);
    }

    public ResponseEntity<Object> getBuyer(long userId) {
        return get("/" + userId);
    }

    public ResponseEntity<Object> getAllBuyers(int minId, int pageSize) {
        Map<String, Object> parameters = Map.of(
                "minId", minId,
                "pageSize", pageSize
        );
        return get("?minId={minId}&pageSize={pageSize}", parameters);
    }

    public ResponseEntity<Object> updateBuyer(long userId, BuyerUpdateDto buyerUpdateDto) {
        return patch("", userId, buyerUpdateDto);
    }

    public ResponseEntity<Object> createFavourite(long userId, long productId) {
        return post("/favorites/" + productId, userId, null);
    }

    public void deleteFavourite(long userId, long productId) {
        delete("/favorites/" + productId, userId);
    }

    public ResponseEntity<Object> getFavourites(long userId) {
        return get("/favorites", userId);
    }

    public ResponseEntity<Object> getRecommendations(long userId, int minId, int pageSize) {
        Map<String, Object> parameters = Map.of(
                "minId", minId,
                "pageSize", pageSize
        );
        return get("/recommendations?minId={minId}&pageSize={pageSize}", userId, parameters);
    }
}