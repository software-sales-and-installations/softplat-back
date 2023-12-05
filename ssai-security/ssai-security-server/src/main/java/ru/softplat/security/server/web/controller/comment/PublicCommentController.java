package ru.softplat.security.server.web.controller.comment;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.softplat.main.client.comment.CommentClient;
import ru.softplat.security.server.message.LogMessage;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/comment")
@Slf4j
@Validated
public class PublicCommentController {

    private final CommentClient commentClient;

    @Operation(summary = "Получение комментария по id", description = "Доступ для всех")
    @GetMapping(path = "/{commentId}")
    public ResponseEntity<Object> getCommentById(@PathVariable Long commentId) {
        log.debug(LogMessage.TRY_GET_COMMENT_PUBLIC.label);
        return commentClient.getCommentById(commentId);
    }

    @Operation(summary = "Получение всех комментариев к продукту", description = "Доступ для всех")
    @GetMapping(path = "/comments/{productId}")
    public ResponseEntity<Object> getAllComments(@PathVariable Long productId) {
        log.debug(LogMessage.TRY_GET_ALL_COMMENTS_PUBLIC.label);
        return commentClient.getAllComments(productId);
    }
}
