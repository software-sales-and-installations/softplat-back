package ru.yandex.workshop.main.controller.basket;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import ru.yandex.workshop.main.dto.basket.BasketDto;
import ru.yandex.workshop.main.dto.buyer.BuyerDto;
import ru.yandex.workshop.main.dto.buyer.BuyerResponseDto;
import ru.yandex.workshop.main.exception.UserNotFoundException;
import ru.yandex.workshop.main.model.product.Category;
import ru.yandex.workshop.main.model.product.License;
import ru.yandex.workshop.main.model.product.Product;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class BuyerBasketControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static Product product;
    private static BuyerResponseDto buyerDto;

    @BeforeEach
    void init() {
        product = Product.builder()
                .id(1L)
                .name("pr")
                .category(Category.builder()
                        .id(1L)
                        .name("Office")
                        .build())
                .description("descr")
                .license(License.LICENSE)
                .price(1234.2F)
                .sellerId(2L)
                .build();
        buyerDto = BuyerResponseDto.builder()
                .firstName("Joe")
                .lastName("Doe")
                .email("joedoe@email.com")
                .telephone("0123456789")
                .build();
    }

    @Test
    @SneakyThrows
    void addProductInBasket_whenOK_returnBasket() {
        BasketDto response = addProduct(1L, 2L);
        assertEquals(buyerDto.getFirstName(), response.getBuyerResponseDto().getFirstName());
        assertEquals(buyerDto.getLastName(), response.getBuyerResponseDto().getLastName());
        assertEquals(buyerDto.getEmail(), response.getBuyerResponseDto().getEmail());
        assertEquals(buyerDto.getTelephone(), response.getBuyerResponseDto().getTelephone());
        assertEquals(1, response.getProductsInBasket().get(0).getQuantity());
    }
/*

    @Test
    @SneakyThrows
    void addNewBuyer_whenEmailNotUnique_thenThrowDuplicateException() {
        createBuyer(buyerDto);
        BuyerDto newBuyerDto = BuyerDto.builder()
                .firstName("Foo")
                .lastName("Bar")
                .telephone("0123456789")
                .email("joedoe@email.com")
                .build();

        mockMvc.perform(post("/buyer")
                        .content(objectMapper.writeValueAsString(newBuyerDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(result -> assertTrue(result.getResolvedException()
                        instanceof DuplicateException));
    }

    @Test
    @SneakyThrows
    void getBuyerById_whenIdCorrect_thenReturnBuyer() {
        createBuyer(buyerDto);
        final long id = 1;

        BuyerResponseDto response = getBuyer(id);
        assertEquals(buyerDto.getFirstName(), response.getFirstName());
        assertEquals(buyerDto.getLastName(), response.getLastName());
        assertEquals(buyerDto.getEmail(), response.getEmail());
        assertEquals(buyerDto.getTelephone(), response.getTelephone());
    }

    @Test
    @SneakyThrows
    void getBuyerById_whenIdIsNotCorrect_thenThrowException() {
        createBuyer(buyerDto);
        final long id = 2;

        mockMvc.perform(get("/buyer/{buyerId}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(result -> assertTrue(result.getResolvedException()
                        instanceof UserNotFoundException));
    }

    @Test
    @SneakyThrows
    void updateBuyerById_whenIsCorrect_thenUpdateBuyer() {
        createBuyer(buyerDto);
        BuyerDto updateDto = BuyerDto.builder()
                .firstName("Foo")
                .build();
        final long id = 1;

        BuyerResponseDto response = updateBuyer(updateDto, id);
        assertEquals(1, response.getId());
        assertEquals(updateDto.getFirstName(), response.getFirstName());
        assertEquals(buyerDto.getLastName(), response.getLastName());
        assertEquals(buyerDto.getEmail(), response.getEmail());
        assertEquals(buyerDto.getTelephone(), response.getTelephone());
    }
*/

    @Test
    @SneakyThrows
    void updateBuyerById_whenIdIsNotCorrect_thenThrowUserNotFoundException() {

        BuyerDto updateDto = BuyerDto.builder()
                .firstName("Foo")
                .lastName("Bar")
                .telephone("0123456789")
                .email("foobar@email.com")
                .build();
        final long id = 2;

        mockMvc.perform(patch("/buyer/{buyerId}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(result -> assertTrue(result.getResolvedException()
                        instanceof UserNotFoundException));
    }

    BasketDto addProduct(Long userId, Long productId) throws Exception {
        MvcResult result = mockMvc.perform(post("/{userId}/basket/{productId}", userId, productId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.buyerResponseDto.firstName").value(buyerDto.getFirstName()))
                .andExpect(jsonPath("$.buyerResponseDto.lastName").value(buyerDto.getLastName()))
                .andExpect(jsonPath("$.buyerResponseDto.email").value(buyerDto.getEmail()))
                .andExpect(jsonPath("$.buyerResponseDto.telephone").value(buyerDto.getTelephone()))
                .andExpect(jsonPath("$.productsInBasket[0].quantity").value(1))
                .andReturn();

        return objectMapper.readValue(
                result.getResponse().getContentAsString(),
                BasketDto.class
        );
    }

    BuyerResponseDto getBuyer(long buyerId) throws Exception {
        MvcResult result = mockMvc.perform(get("/buyer/{buyerId}", buyerId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        return objectMapper.readValue(
                result.getResponse().getContentAsString(),
                BuyerResponseDto.class
        );
    }

    BuyerResponseDto updateBuyer(BuyerDto updateDto, long buyerId) throws Exception {
        MvcResult result = mockMvc.perform(post("/buyer/{buyerId}", buyerId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstName").value(updateDto.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(buyerDto.getLastName()))
                .andExpect(jsonPath("$.email").value(buyerDto.getEmail()))
                .andExpect(jsonPath("$.telephone").value(buyerDto.getTelephone()))
                .andExpect(jsonPath("$.productsInBasket[0].quantity").value(1))
                .andReturn();

        return objectMapper.readValue(
                result.getResponse().getContentAsString(),
                BuyerResponseDto.class
        );
    }

}
