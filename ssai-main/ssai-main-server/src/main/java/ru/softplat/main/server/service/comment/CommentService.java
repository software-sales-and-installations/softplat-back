package ru.softplat.main.server.service.comment;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.softplat.main.server.configuration.PageRequestOverride;
import ru.softplat.main.server.exception.AccessDenialException;
import ru.softplat.main.server.exception.DuplicateException;
import ru.softplat.main.server.exception.EntityNotFoundException;
import ru.softplat.main.server.message.ExceptionMessage;
import ru.softplat.main.server.model.buyer.Buyer;
import ru.softplat.main.server.model.comment.Comment;
import ru.softplat.main.server.model.product.Product;
import ru.softplat.main.server.repository.comment.CommentRepository;
import ru.softplat.main.server.service.buyer.BuyerService;
import ru.softplat.main.server.service.buyer.OrderService;
import ru.softplat.main.server.service.product.ProductService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final ProductService productService;
    private final BuyerService buyerService;
    private final OrderService orderService;

    public Comment createComment(Comment comment, long productId, long authorId) {
        Buyer author = buyerService.getBuyer(authorId);
        Product product = productService.getAvailableProduct(productId);

        orderService.checkBuyerAccessRightsToCreateComment(authorId, productId);
        checkIfCommentByAuthorOnProductExists(authorId, productId);

        comment.setAuthor(author);
        comment.setProduct(product);
        comment.setDate(LocalDateTime.now());

        long ratingCount = countComments(productId);
        productService.updateProductRatingOnCommentCreate(productId, comment.getRating(), ratingCount);

        return commentRepository.save(comment);
    }

    @Transactional(readOnly = true)
    public Comment getCommentById(long commentId) {
        String message = ExceptionMessage.ENTITY_NOT_FOUND_EXCEPTION.getMessage(commentId, Comment.class);
        Optional<Comment> comment = commentRepository.findById(commentId);
        return comment.orElseThrow(() -> new EntityNotFoundException(message));
    }

    public Comment updateCommentByAuthor(long commentId, long authorId, Comment commentForUpdate) {
        Comment comment = getCommentById(commentId);
        checkBuyerAccessRightsToUpdateComment(commentId, authorId);

        if (commentForUpdate.getText() != null && !commentForUpdate.getText().isEmpty()) {
            comment.setText(commentForUpdate.getText());
        }
        if (commentForUpdate.getRating() != null) {
            float ratingUpdate = commentForUpdate.getRating();
            float oldRating = comment.getRating();
            long productId = comment.getProduct().getId();
            long commentCount = countComments(productId);

            productService.updateProductRatingOnPatch(productId, ratingUpdate, commentCount, oldRating);
            comment.setRating(commentForUpdate.getRating());
        }

        return commentRepository.save(comment);
    }

    public void deleteComment(long commentId) {
        Comment comment = getCommentById(commentId);
        float rating = comment.getRating();
        long productId = comment.getProduct().getId();
        long commentCount = countComments(productId);

        productService.updateProductRatingOnCommentDelete(productId, rating, commentCount);
        commentRepository.deleteById(commentId);
    }

    @Transactional(readOnly = true)
    public List<Comment> getAllComments(long productId, int minId, int pageSize) {
        Sort sort = Sort.by("date").descending();
        PageRequest page = PageRequestOverride.of(minId, pageSize, sort);
        return commentRepository.findAllByProductId(productId, page);
    }

    @Transactional(readOnly = true)
    public long countComments(long productId) {
        return commentRepository.countAllByProductId(productId);
    }

    @Transactional(readOnly = true)
    public void checkBuyerAccessRightsToUpdateComment(long commentId, long authorId) {
        Comment comment = getCommentById(commentId);
        if (!comment.getAuthor().getId().equals(authorId)) {
            throw new AccessDenialException(ExceptionMessage.NO_RIGHTS_COMMENT_EXCEPTION.label);
        }
    }

    private void checkIfCommentByAuthorOnProductExists(long authorId, long productId) {
        if (commentRepository.existsByAuthorIdAndProductId(authorId, productId)) {
            throw new DuplicateException(ExceptionMessage.DUPLICATE_EXCEPTION.label);
        }
    }
}
