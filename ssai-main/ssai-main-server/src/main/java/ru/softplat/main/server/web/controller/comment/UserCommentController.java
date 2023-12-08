package ru.softplat.main.server.web.controller.comment;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.softplat.main.dto.comment.CommentCreateUpdateDto;
import ru.softplat.main.dto.comment.CommentResponseDto;
import ru.softplat.main.server.mapper.CommentMapper;
import ru.softplat.main.server.model.comment.Comment;
import ru.softplat.main.server.service.comment.CommentService;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/comment")
public class UserCommentController {
    private final CommentService commentService;
    private final CommentMapper commentMapper;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(path = "/{productId}/create")
    public CommentResponseDto createComment(@RequestHeader("X-Sharer-User-Id") long userId,
                                            @PathVariable Long productId,
                                            @RequestBody CommentCreateUpdateDto commentDto) {
        Comment request = commentMapper.commentDtoToComment(commentDto);
        Comment response = commentService.createComment(request, productId, userId);
        return commentMapper.commentToCommentResponseDto(response);
    }

    @PatchMapping(path = "/{commentId}/update")
    public CommentResponseDto updateCommentByAuthor(@RequestHeader("X-Sharer-User-Id") long userId,
                                                    @PathVariable Long commentId,
                                                    @RequestBody CommentCreateUpdateDto updateDto) {
        Comment request = commentMapper.commentDtoToComment(updateDto);
        Comment response = commentService.updateCommentByAuthor(commentId, userId, request);
        return commentMapper.commentToCommentResponseDto(response);
    }

    @DeleteMapping(path = "/{commentId}/delete")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteCommentByAuthor(@RequestHeader("X-Sharer-User-Id") long userId,
                                      @PathVariable Long commentId) {
        commentService.checkBuyerAccessRightsToUpdateComment(commentId, userId);
        commentService.deleteComment(commentId);
    }

    @DeleteMapping(path = "/{commentId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteCommentByAdmin(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);
    }
}
