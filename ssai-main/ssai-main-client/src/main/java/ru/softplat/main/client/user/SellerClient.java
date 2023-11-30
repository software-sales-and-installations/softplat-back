package ru.softplat.main.client.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.softplat.main.client.BaseClient;
import ru.softplat.main.dto.seller.BankRequisitesCreateUpdateDto;
import ru.softplat.main.dto.user.SellerUpdateDto;
import ru.softplat.security.dto.UserCreateMainDto;

import java.util.Map;

@Service
@Slf4j
public class SellerClient extends BaseClient {
    private static final String API_PREFIX = "/seller";

    @Autowired
    public SellerClient(@Value("${main-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .build()
        );
    }

    public ResponseEntity<Object> createSeller(UserCreateMainDto userCreateMainDto) {
        return post("", userCreateMainDto);
    }

    public ResponseEntity<Object> getSeller(long userId) {
        return get("/" + userId);
    }

    public ResponseEntity<Object> getAllSellers(int minId, int pageSize) {
        Map<String, Object> parameters = Map.of(
                "minId", minId,
                "pageSize", pageSize
        );
        return get("?minId={minId}&pageSize={pageSize}", parameters);
    }

    public ResponseEntity<Object> updateSeller(long userId, SellerUpdateDto sellerUpdateDto) {
        return patch("", userId, sellerUpdateDto);
    }

    public ResponseEntity<Object> getRequisitesAdmin(long userId) {
        return get("/bank/" + userId);
    }

    public ResponseEntity<Object> getRequisitesSeller(long userId) {
        return get("/bank", userId);
    }

    public void deleteRequisites(long userId) {
        delete("/bank", userId);
    }

    public ResponseEntity<Object> updateRequisites(long userId, BankRequisitesCreateUpdateDto requisites) {
        return patch("/bank", userId, requisites);
    }

    public ResponseEntity<Object> addSellerImage(long userId, MultipartFile image) {
        return post("/account/image", userId, image);
    }

    public void deleteOwnImage(long userId) {
        delete("/account/image", userId);
    }

    public void deleteSellerImage(long userId) {
        delete("/" + userId + "/image");
    }
}