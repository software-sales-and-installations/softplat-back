/*
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
}
*/
