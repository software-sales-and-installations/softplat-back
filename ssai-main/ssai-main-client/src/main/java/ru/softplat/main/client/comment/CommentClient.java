package ru.softplat.main.client.comment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.softplat.main.client.BaseClient;
import ru.softplat.main.dto.comment.CommentCreateUpdateDto;

import java.util.Map;

@Component
public class CommentClient extends BaseClient {
    private static final String API_PREFIX = "/comment";

    @Autowired
    public CommentClient(@Value("${main-server.url:http://localhost:8080}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .build()
        );
    }

    public ResponseEntity<Object> createComment(CommentCreateUpdateDto createDto, Long userId, Long productId) {
        return post("/" + productId + "/create", userId, createDto);
    }

    public ResponseEntity<Object> updateCommentByAuthor(CommentCreateUpdateDto updateDto, Long userId, Long commentId) {
        return patch("/" + commentId + "/update", userId, updateDto);
    }

    public ResponseEntity<Object> deleteCommentByAuthor(Long userId, Long commentId) {
        return delete("/" + commentId + "/delete", userId);
    }

    public ResponseEntity<Object> deleteCommentByAdmin(Long commentId) {
        return delete("/" + commentId);
    }

    public ResponseEntity<Object> getCommentById(Long commentId) {
        return get("/" + commentId);
    }

    public ResponseEntity<Object> getAllComments(long productId, int minId, int pageSize) {
        Map<String, Object> parameters = Map.of(
                "minId", minId,
                "pageSize", pageSize
        );
        return get("/" + productId + "/product?minId={minId}&pageSize={pageSize}", parameters);
    }
}
