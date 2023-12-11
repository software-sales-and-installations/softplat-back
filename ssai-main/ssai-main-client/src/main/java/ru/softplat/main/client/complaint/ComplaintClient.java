package ru.softplat.main.client.complaint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.softplat.main.client.BaseClient;
import ru.softplat.main.dto.compliant.ComplaintReason;

@Service
public class ComplaintClient extends BaseClient {
    private static final String API_PREFIX = "/complaint";

    @Autowired
    public ComplaintClient(@Value("${main-server.url:http://localhost:8080}") String serverUrl, RestTemplateBuilder builder) {
        super(builder
                .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                .build());
    }

    public ResponseEntity<Object> createComplaint(Long userId, Long productId, ComplaintReason reason) {
        return post("/buyer/" + userId + "/" + productId + "/complaint&reason={reason}", reason);
    }

    public ResponseEntity<Object> getComplaintListForAdmin() {
        return get("");
    }

    public ResponseEntity<Object> getComplaintListForSeller(Long userId) {
        return get("/seller/" + userId + "/complaints", userId);
    }
}