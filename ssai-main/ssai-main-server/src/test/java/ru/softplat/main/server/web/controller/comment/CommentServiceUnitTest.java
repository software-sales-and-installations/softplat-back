package ru.softplat.main.server.web.controller.comment;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.softplat.main.dto.comment.CommentCreateUpdateDto;
import ru.softplat.main.server.exception.AccessDenialException;
import ru.softplat.main.server.exception.EntityNotFoundException;
import ru.softplat.main.server.model.buyer.Buyer;
import ru.softplat.main.server.model.buyer.Order;
import ru.softplat.main.server.model.buyer.OrderPosition;
import ru.softplat.main.server.model.comment.Comment;
import ru.softplat.main.server.model.product.Product;
import ru.softplat.main.server.repository.comment.CommentRepository;
import ru.softplat.main.server.service.buyer.BuyerService;
import ru.softplat.main.server.service.buyer.OrderService;
import ru.softplat.main.server.service.comment.CommentService;
import ru.softplat.main.server.service.product.ProductService;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CommentServiceUnitTest {
    @Mock
    private BuyerService buyerService;
    @Mock
    private ProductService productService;
    @Mock
    private OrderService orderService;
    @Mock
    private CommentRepository commentRepository;
    @InjectMocks
    private CommentService commentService;

    private static Buyer buyer;
    private static Product product;
    private static Order order;

    private static Validator validator;

    static {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.usingContext().getValidator();
    }

    @BeforeEach
    void init() {
        buyer = Buyer.builder().id(1L).build();
        product = Product.builder().id(1L).build();
        OrderPosition orderPosition = OrderPosition.builder().product(product).build();
        order = Order.builder().buyer(buyer).productsOrdered(List.of(orderPosition)).build();
    }

    @Test
    public void testValidations_showThrowException_whenNoRatingProvided() {
        // given
        CommentCreateUpdateDto request = CommentCreateUpdateDto.builder().text("comment").build();

        // when
        Set<ConstraintViolation<CommentCreateUpdateDto>> violations
                = validator.validate(request, CommentCreateUpdateDto.New.class);

        // then
        assertEquals(1, violations.size());
        ConstraintViolation<CommentCreateUpdateDto> violation = violations.iterator().next();
        assertEquals("Необходимо указать оценку товара от 1 до 5.", violation.getMessage());
    }

    @Test
    void createComment_shouldReturnComment_whenPassedValidRequestDto() {
        // given
        Comment request = Comment.builder().author(buyer).text("comment").rating(4.5F).build();

        // when
        when(buyerService.getBuyer(buyer.getId())).thenReturn(buyer);
        when(productService.getAvailableProduct(product.getId())).thenReturn(product);
        when(orderService.getAllOrders(buyer.getId())).thenReturn(List.of(order));
        when(commentRepository.save(any())).thenReturn(request);

        // then
        Comment response = commentService.createComment(request, product.getId(), buyer.getId());
        assertEquals(request, response);
    }

    @Test
    void createComment_shouldThrowException_whenUserDidntBuyProduct() {
        // given
        Buyer otherBuyer = Buyer.builder().id(2L).build();
        Comment request = Comment.builder().author(otherBuyer).text("comment").rating(4.5F).build();

        // when
        when(buyerService.getBuyer(otherBuyer.getId())).thenReturn(otherBuyer);
        when(productService.getAvailableProduct(product.getId())).thenReturn(product);
        when(orderService.getAllOrders(otherBuyer.getId())).thenReturn(Lists.emptyList());

        // then
        assertThrows(AccessDenialException.class,
                () -> commentService.createComment(request, product.getId(), otherBuyer.getId()));
    }

    @Test
    void getCommentById_shouldThrowException_whenIdInvalid() {
        // when
        when(commentRepository.findById(anyLong())).thenReturn(Optional.empty());

        // then
        assertThrows(EntityNotFoundException.class, () -> commentService.getCommentById(123L));
    }

    @Test
    void updateCommentByAuthor_shouldUpdateCorrectly_whenValidUser() {
        // given
        Comment oldComment = Comment.builder().id(1L).author(buyer).product(product).text("old comment").rating(3.5F).build();
        Comment updateRequest = Comment.builder().text("comment").rating(4.5F).build();
        oldComment.setText(updateRequest.getText());
        oldComment.setRating(updateRequest.getRating());

        // when
        when(commentRepository.findById(oldComment.getId())).thenReturn(Optional.of(oldComment));
        when(commentRepository.save(any())).thenReturn(oldComment);

        // then
        Comment response = commentService.updateCommentByAuthor(oldComment.getId(), buyer.getId(), updateRequest);
        assertEquals(oldComment.getAuthor(), response.getAuthor());
        assertEquals(oldComment.getText(), response.getText());
        assertEquals(oldComment.getRating(), response.getRating());
    }

    @Test
    void updateCommentByAuthor_shouldThrowException_whenUserIsNotAuthor() {
        // given
        long userId = 2L;
        Comment oldComment = Comment.builder().id(1L).author(buyer).text("old comment").rating(3.5F).build();
        Comment updateRequest = Comment.builder().text("comment").rating(4.5F).build();

        // when
        when(commentRepository.findById(oldComment.getId())).thenReturn(Optional.of(oldComment));

        // then
        assertThrows(AccessDenialException.class,
                () -> commentService.updateCommentByAuthor(oldComment.getId(), userId, updateRequest));
    }

    @Test
    void deleteCommentByAuthor_shouldThrowException_whenUserNotAuthor() {
        // given
        long userId = 2L;
        Comment comment = Comment.builder().id(1L).author(buyer).text("old comment").rating(3.5F).build();

        // when
        when(commentRepository.findById(comment.getId())).thenReturn(Optional.of(comment));

        // then
        assertThrows(AccessDenialException.class,
                () -> commentService.checkBuyerAccessRightsToUpdateComment(comment.getId(), userId));
    }
}