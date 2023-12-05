package ru.softplat.main.server.repository.comment;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.softplat.main.server.model.comment.Comment;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByProductIdOrderByDateDesc(long productId);

    Long countAllByProductId(long productId);

    boolean existsByAuthorIdAndProductId(long authorId, long productId);
}
