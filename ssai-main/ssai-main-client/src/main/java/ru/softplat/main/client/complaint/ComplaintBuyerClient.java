package ru.softplat.main.client.complaint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.softplat.main.client.BaseClient;

import java.util.Map;

@Service
public class ComplaintBuyerClient extends BaseClient {
    private static final String API_PREFIX = "complaint/buyer";

    @Autowired
    public ComplaintBuyerClient(@Value("${main-server.url:http://localhost:8080}") String serverUrl, RestTemplateBuilder builder) {
        super(builder
                .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                .build());
    }

    public ResponseEntity<Object> getAllComplaintReasons() {
        return get("/complaints");
    }

    public ResponseEntity<Object> createComplaint(Long userId, Long productId, String reason) {
        Map<String, Object> parameters = Map.of(
                "userId", userId,
                "productId", productId,
                "reason", reason
        );

        return post("/{userId}/{productId}/complaint", parameters);
    }
}
