package ru.softplat.main.server.web.controller.comment;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import ru.softplat.main.server.model.buyer.Buyer;
import ru.softplat.main.server.model.comment.Comment;
import ru.softplat.main.server.model.product.Product;
import ru.softplat.main.server.service.buyer.BuyerService;
import ru.softplat.main.server.service.comment.CommentService;
import ru.softplat.main.server.service.product.ProductService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;


@SpringBootTest
@Sql("/data-test.sql")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class CommentServiceIntegrationTest {
    @Autowired
    private CommentService commentService;
    @Autowired
    private BuyerService buyerService;
    @Autowired
    private ProductService productService;

    static Buyer buyer1, buyer2, buyer3;
    static Product product1;
    static Comment comment1, comment2, comment3;

    @BeforeEach
    void init() {
        product1 = productService.getAvailableProduct(1L);
        buyer1 = buyerService.getBuyer(1L);
        buyer2 = buyerService.getBuyer(2L);
        buyer3 = buyerService.getBuyer(3L);
    }

    @Test
    void getAllComments_shouldReturnThreeComments_whenPostedByBuyers() {
        // given
        initComments();
        int from = 0, size = 5;

        // then
        List<Comment> expect = List.of(comment3, comment2, comment1);
        List<Comment> actual = commentService.getAllComments(product1.getId(), from, size);

        performAssertions(expect, actual);
    }

    @Test
    void createComment_shouldReturnProductAverageRatingThreeFloat_whenTheeUsersRate() {
        // given
        initComments();

        // then
        float expectRating = 3F;
        float actualRating = productService.getAvailableProduct(product1.getId()).getRating();

        assertEquals(expectRating, actualRating);
    }

    @Test
    void countComments_shouldReturnCountThreeLong_whenPostedByBuyers() {
        // given
        initComments();

        // then
        long expect = 3L;
        long actual = commentService.countComments(product1.getId());

        assertEquals(expect, actual);
    }

    @Test
    void deleteComment_shouldReturnCountZero_whenCommentsDeleted() {
        // given
        initComments();

        // when
        commentService.deleteComment(1L);
        commentService.deleteComment(2L);
        commentService.deleteComment(3L);

        // then
        long expect = 0L;
        long actual = commentService.countComments(product1.getId());

        assertEquals(expect, actual);
    }

    @Test
    void deleteComment_shouldUpdateProductRating_whenCommentDeleted() {
        // given
        initComments();

        // when
        commentService.deleteComment(3L);

        // then
        float expect = 3.25F;
        float actual = productService.getAvailableProduct(product1.getId()).getRating();

        assertEquals(expect, actual);
    }

    @Test
    void deleteComment_shouldReturnRatingNull_whenAllCommentsDeleted() {
        // given
        initComments();

        // when
        commentService.deleteComment(1L);
        commentService.deleteComment(2L);
        commentService.deleteComment(3L);

        // then
        assertNull(productService.getAvailableProduct(product1.getId()).getRating());
    }

    void initComments() {
        comment1 = Comment.builder().rating(3.5F).text("comment 1").build();
        comment2 = Comment.builder().rating(3.0F).text("comment 2").build();
        comment3 = Comment.builder().rating(2.5F).text("comment 3").build();

        commentService.createComment(comment1, product1.getId(), buyer1.getId());
        commentService.createComment(comment2, product1.getId(), buyer2.getId());
        commentService.createComment(comment3, product1.getId(), buyer3.getId());
    }

    void performAssertions(List<Comment> actual, List<Comment> expect) {
        assertEquals(expect.size(), actual.size());

        for (int i = 0; i < expect.size(); i++) {
            Comment expectComment = expect.get(i);
            Comment actualComment = actual.get(i);
            assertEquals(expectComment.getId(), actualComment.getId());
            assertEquals(expectComment.getText(), actualComment.getText());
            assertEquals(expectComment.getRating(), actualComment.getRating());
            assertEquals(expectComment.getProduct().getId(), actualComment.getProduct().getId());
            assertEquals(expectComment.getAuthor().getId(), actualComment.getAuthor().getId());
        }
    }
}
