package ru.softplat.security.server.web.controller.comment;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.softplat.main.client.comment.CommentClient;
import ru.softplat.main.dto.comment.CommentCreateUpdateDto;
import ru.softplat.security.server.message.LogMessage;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/comment")
@Slf4j
public class UserCommentController {

    private final CommentClient commentClient;

    @Operation(summary = " Создание комментария к продукту", description = "Доступ для покупателя")
    @PreAuthorize("hasAuthority('buyer:write')")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(path = "/{productId}/create")
    public ResponseEntity<Object> createComment(
            @RequestHeader("X-Sharer-User-Id") long userId,
            @PathVariable Long productId,
            @RequestBody @Validated(CommentCreateUpdateDto.New.class) CommentCreateUpdateDto createDto
    ) {
        log.debug(LogMessage.TRY_CREATE_COMMENT.label, createDto);
        return commentClient.createComment(createDto, userId, productId);
    }

    @Operation(summary = " Обновление комментария к продукту", description = "Доступ для покупателя")
    @PreAuthorize("hasAuthority('buyer:write')")
    @PatchMapping(path = "/{commentId}/update")
    public ResponseEntity<Object> updateCommentByAuthor(
            @RequestHeader("X-Sharer-User-Id") long userId,
            @PathVariable Long commentId,
            @RequestBody @Validated(CommentCreateUpdateDto.Update.class) CommentCreateUpdateDto updateDto
    ) {
        log.debug(LogMessage.TRY_UPDATE_COMMENT.label, updateDto);
        return commentClient.updateCommentByAuthor(updateDto, userId, commentId);
    }

    @Operation(summary = " Удаление комментария к продукту покупателем", description = "Доступ для покупателя")
    @PreAuthorize("hasAuthority('buyer:write')")
    @DeleteMapping(path = "/comments/{commentId}/delete")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public ResponseEntity<Object> deleteCommentByAuthor(@RequestHeader("X-Sharer-User-Id") long userId,
                                      @PathVariable Long commentId) {
        log.debug(LogMessage.TRY_DELETE_COMMENT_BUYER.label);
        return commentClient.deleteCommentByAuthor(userId, commentId);
    }

    @Operation(summary = " Удаление комментария к продукту админом", description = "Доступ для админа")
    @PreAuthorize("hasAuthority('admin:write')")
    @DeleteMapping(path = "/{commentId}/delete")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public ResponseEntity<Object> deleteCommentByAdmin(@PathVariable Long commentId) {
        log.debug(LogMessage.TRY_DELETE_COMMENT_ADMIN.label);
        return commentClient.deleteCommentByAdmin(commentId);
    }

}
