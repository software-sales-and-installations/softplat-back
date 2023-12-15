package ru.softplat.main.client.complaint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.softplat.main.client.BaseClient;
import ru.softplat.main.dto.compliant.ComplaintReason;
import ru.softplat.main.dto.compliant.ComplaintUpdateDto;

import java.util.Map;

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
        return post("/" + productId + "?reason={reason}", userId, Map.of("reason", String.valueOf(reason)));
    }

    public ResponseEntity<Object> getComplaintListForAdmin(int minId, int pageSize) {
        Map<String, Object> parameters = getParameters(minId, pageSize);
        return get("/admin?minId={minId}&pageSize={pageSize}", parameters);
    }

    public ResponseEntity<Object> getComplaintsForProductByAdmin(long productId, int minId, int pageSize) {
        Map<String, Object> parameters = getParameters(minId, pageSize);
        return get("/admin/" + productId + "/product?minId={minId}&pageSize={pageSize}", parameters);
    }

    public ResponseEntity<Object> getComplaintByIdByAdmin(long complaintId) {
        return get("/admin/" + complaintId);
    }

    public ResponseEntity<Object> sendProductOnModerationByAdmin(long complaintId, ComplaintUpdateDto updateDto) {
        return patch("/admin/" + complaintId, updateDto);
    }

    public ResponseEntity<Object> getComplaintListForSeller(long userId, int minId, int pageSize) {
        Map<String, Object> parameters = getParameters(minId, pageSize);
        return get("/seller?minId={minId}&pageSize={pageSize}", userId, parameters);
    }

    public ResponseEntity<Object> getComplaintsForProductBySeller(long userId, long productId, int minId, int pageSize) {
        Map<String, Object> parameters = getParameters(minId, pageSize);
        return get("/seller/" + productId + "/product?minId={minId}&pageSize={pageSize}", userId, parameters);
    }

    public ResponseEntity<Object> getComplaintById(long userId, long complaintId) {
        return get("/seller/" + complaintId, userId);
    }

    private Map<String, Object> getParameters(int minId, int pageSize) {
        return Map.of(
                "minId", minId,
                "pageSize", pageSize
        );
    }
}
