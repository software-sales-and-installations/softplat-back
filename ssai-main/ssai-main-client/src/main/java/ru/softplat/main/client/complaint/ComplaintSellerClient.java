package ru.softplat.main.client.complaint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.softplat.main.client.BaseClient;

@Service
public class ComplaintSellerClient extends BaseClient {
    private static final String API_PREFIX = "complaint/seller";

    @Autowired
    public ComplaintSellerClient(@Value("${main-server.url:http://localhost:8080}") String serverUrl, RestTemplateBuilder builder) {
        super(builder
                .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                .build());
    }

    public ResponseEntity<Object> getComplaintListForSeller(Long userId) {
        return get("/{userId}/complaints", userId);
    }
}
