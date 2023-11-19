package ru.yandex.workshop.main.controller.basket;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import ru.yandex.workshop.main.controller.CrudOperations;
import ru.yandex.workshop.main.dto.basket.BasketResponseDto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@Sql("/data-test.sql")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class BuyerBasketControllerTest extends CrudOperations {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @SneakyThrows
    @WithMockUser(username = "buyer1@email.ru", authorities = {"buyer:write"})
    void getBasket_whenEmptyBasket_returnNewBasketResponse() {
        BasketResponseDto basketResponseDto = getBasket();

        assertEquals(1L, basketResponseDto.getBuyerId());
        assertNull(basketResponseDto.getProductsInBasket());
    }

    @Test
    @SneakyThrows
    @WithMockUser(username = "buyer1@email.ru", authorities = {"buyer:write"})
    void addProductInBasket_whenEmptyBasket_returnBasketResponseDtoWithOneProduct() {
        addProductInBasket(1L, false);
        BasketResponseDto basketResponseDto = getBasket();

        assertEquals(1L, basketResponseDto.getBuyerId());
        assertEquals(1, basketResponseDto.getProductsInBasket().size());
        assertEquals(1L, basketResponseDto.getProductsInBasket().get(0).getProductResponseDto().getId());
        assertEquals(1, basketResponseDto.getProductsInBasket().get(0).getQuantity());
        assertEquals(false, basketResponseDto.getProductsInBasket().get(0).getInstallation());
    }

    @Test
    @SneakyThrows
    @WithMockUser(username = "buyer1@email.ru", authorities = {"buyer:write"})
    void addProductInBasket_whenHaveOne_returnBasketResponseDtoWithOnePosition() {
        addProductInBasket(1L, false);
        BasketResponseDto basketResponseDto = addProductInBasket(2L, false);

        assertEquals(1L, basketResponseDto.getBuyerId());
        assertEquals(2, basketResponseDto.getProductsInBasket().size());
        assertEquals(1L, basketResponseDto.getProductsInBasket().get(0).getProductResponseDto().getId());
        assertEquals(1, basketResponseDto.getProductsInBasket().get(0).getQuantity());
        assertEquals(false, basketResponseDto.getProductsInBasket().get(0).getInstallation());
        assertEquals(2L, basketResponseDto.getProductsInBasket().get(1).getProductResponseDto().getId());
        assertEquals(1, basketResponseDto.getProductsInBasket().get(1).getQuantity());
        assertEquals(false, basketResponseDto.getProductsInBasket().get(1).getInstallation());
    }

    @Test
    @SneakyThrows
    @WithMockUser(username = "buyer1@email.ru", authorities = {"buyer:write"})
    void deleteProductFromBasket_whenOne_returnEmptyBasket() {
        addProductInBasket(1L, false);
        MvcResult result = mockMvc.perform(delete("/buyer/basket/{productId}", 1L))
                .andExpect(status().isOk())
                .andReturn();
        BasketResponseDto basketResponseDto = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                BasketResponseDto.class);

        assertEquals(1L, basketResponseDto.getBuyerId());
        assertEquals(0, basketResponseDto.getProductsInBasket().size());
    }

    @Test
    @SneakyThrows
    @WithMockUser(username = "buyer1@email.ru", authorities = {"buyer:write"})
    void deleteProductFromBasket_whenTwo_returnOnePosition() {
        addProductInBasket(1L, false);
        addProductInBasket(1L, false);
        MvcResult result = mockMvc.perform(delete("/buyer/basket/{productId}", 1L))
                .andExpect(status().isOk())
                .andReturn();
        BasketResponseDto basketResponseDto = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                BasketResponseDto.class);

        assertEquals(1L, basketResponseDto.getBuyerId());
        assertEquals(1, basketResponseDto.getProductsInBasket().size());
        assertEquals(1L, basketResponseDto.getProductsInBasket().get(0).getProductResponseDto().getId());
        assertEquals(1, basketResponseDto.getProductsInBasket().get(0).getQuantity());
        assertEquals(false, basketResponseDto.getProductsInBasket().get(0).getInstallation());
    }
}

