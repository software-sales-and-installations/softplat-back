package ru.softplat.main.server.web.controller.comment;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.softplat.main.dto.comment.CommentListResponseDto;
import ru.softplat.main.dto.comment.CommentResponseDto;
import ru.softplat.main.server.mapper.CommentMapper;
import ru.softplat.main.server.model.comment.Comment;
import ru.softplat.main.server.service.comment.CommentService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/comment")
public class PublicCommentController {
    private final CommentService commentService;
    private final CommentMapper commentMapper;

    @GetMapping(path = "/{commentId}")
    public CommentResponseDto getCommentById(@PathVariable Long commentId) {
        Comment response = commentService.getCommentById(commentId);
        return commentMapper.commentToCommentResponseDto(response);
    }

    @GetMapping(path = "/{productId}/product")
    public CommentListResponseDto getAllComments(@PathVariable Long productId,
                                                 @RequestParam int minId,
                                                 @RequestParam int pageSize) {
        List<Comment> commentList = commentService.getAllComments(productId, minId, pageSize);
        List<CommentResponseDto> response = commentList.stream()
                .map(commentMapper::commentToCommentResponseDto)
                .collect(Collectors.toList());
        long totalCount = commentService.countComments(productId);
        return commentMapper.toCommentsListResponseDto(response, totalCount);
    }
}
