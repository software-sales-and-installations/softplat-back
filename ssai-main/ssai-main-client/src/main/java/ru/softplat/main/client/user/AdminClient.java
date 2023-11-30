package ru.softplat.main.client.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.softplat.main.client.BaseClient;
import ru.softplat.security.dto.UserCreateMainDto;

@Service
public class AdminClient extends BaseClient {
    private static final String API_PREFIX = "/admin";

    @Autowired
    public AdminClient(@Value("${main-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .build()
        );
    }

    public ResponseEntity<Object> addAdmin(UserCreateMainDto admin) {
        return post("", admin);
    }

    public ResponseEntity<Object> getAdminById(Long adminId) {
        return get("", adminId);
    }

}
