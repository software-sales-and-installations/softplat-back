package ru.softplat.main.server.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;
import ru.softplat.main.dto.comment.CommentCreateUpdateDto;
import ru.softplat.main.dto.comment.CommentListResponseDto;
import ru.softplat.main.dto.comment.CommentResponseDto;
import ru.softplat.main.server.model.comment.Comment;

import java.util.List;

@Mapper(uses = {ProductMapper.class, BuyerMapper.class})
@Component
public interface CommentMapper {

    @Mapping(target = "author", ignore = true)
    @Mapping(target = "product", ignore = true)
    @Mapping(target = "date", ignore = true)
    Comment commentDtoToComment(CommentCreateUpdateDto commentCreateUpdateDto);

    CommentResponseDto commentToCommentResponseDto(Comment comment);

    default CommentListResponseDto toCommentsListResponseDto(List<CommentResponseDto> comments, long count) {
        return CommentListResponseDto.builder().comments(comments).totalComments(count).build();
    }
}
