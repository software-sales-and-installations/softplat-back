package ru.softplat.main.server.main.controller.basket;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import ru.yandex.workshop.main.controller.AbstractControllerTest;
import ru.yandex.workshop.main.dto.basket.BasketResponseDto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@Sql("/data-test.sql")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class BuyerBasketControllerTest extends AbstractControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @SneakyThrows
    @WithMockUser(username = "buyer1@email.ru", authorities = {"buyer:write"})
    void getBasket_shouldReturnNewBasketResponse_whenEmptyBasket() {
        BasketResponseDto actualBasketResponseDto = getBasket();

        assertEquals(1L, actualBasketResponseDto.getBuyerId());
        assertEquals(0, actualBasketResponseDto.getProductsInBasket().size());
    }

    @Test
    @SneakyThrows
    @WithMockUser(username = "buyer1@email.ru", authorities = {"buyer:write"})
    void addProductInBasket_shouldReturnBasketResponseDtoWithOneProduct_whenEmptyBasket() {
        BasketResponseDto actualBasketResponseDto = addProductInBasket(1L, false);

        assertEquals(1L, actualBasketResponseDto.getId());
        assertEquals(1L, actualBasketResponseDto.getBuyerId());
        assertEquals(1, actualBasketResponseDto.getProductsInBasket().size());
        assertEquals(1L, actualBasketResponseDto.getProductsInBasket().get(0).getProductResponseDto().getId());
        assertEquals(1, actualBasketResponseDto.getProductsInBasket().get(0).getQuantity());
        assertEquals(false, actualBasketResponseDto.getProductsInBasket().get(0).getInstallation());
    }

    @Test
    @SneakyThrows
    @WithMockUser(username = "buyer1@email.ru", authorities = {"buyer:write"})
    void addProductInBasket_shouldReturnBasketResponseDtoWithOnePosition_whenHaveOneProduct() {
        addProductInBasket(1L, false);
        BasketResponseDto actualBasketResponseDto = addProductInBasket(2L, false);

        assertEquals(1L, actualBasketResponseDto.getBuyerId());
        assertEquals(2, actualBasketResponseDto.getProductsInBasket().size());
        assertEquals(1L, actualBasketResponseDto.getProductsInBasket().get(0).getProductResponseDto().getId());
        assertEquals(1, actualBasketResponseDto.getProductsInBasket().get(0).getQuantity());
        assertEquals(false, actualBasketResponseDto.getProductsInBasket().get(0).getInstallation());
        assertEquals(2L, actualBasketResponseDto.getProductsInBasket().get(1).getProductResponseDto().getId());
        assertEquals(1, actualBasketResponseDto.getProductsInBasket().get(1).getQuantity());
        assertEquals(false, actualBasketResponseDto.getProductsInBasket().get(1).getInstallation());
    }

    @Test
    @SneakyThrows
    @WithMockUser(username = "buyer1@email.ru", authorities = {"buyer:write"})
    void deleteProductFromBasket_shouldReturnEmptyBasket_whenOneProduct() {
        addProductInBasket(1L, false);
        MvcResult result = mockMvc.perform(delete("/buyer/basket/{productId}", 1L))
                .andExpect(status().isOk())
                .andReturn();
        BasketResponseDto basketResponseDto = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                BasketResponseDto.class);

        assertEquals(1L, basketResponseDto.getBuyerId());
        assertEquals(1, basketResponseDto.getProductsInBasket().size());
    }

    @Test
    @SneakyThrows
    @WithMockUser(username = "buyer1@email.ru", authorities = {"buyer:write"})
    void deleteProductFromBasket_shouldReturnOnePosition_whenTwoProducts() {
        addProductInBasket(1L, false);
        addProductInBasket(1L, false);
        MvcResult result = mockMvc.perform(delete("/buyer/basket/{productId}", 1L))
                .andExpect(status().isOk())
                .andReturn();
        BasketResponseDto actualBasketResponseDto = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                BasketResponseDto.class);

        assertEquals(1L, actualBasketResponseDto.getBuyerId());
        assertEquals(1, actualBasketResponseDto.getProductsInBasket().size());
        assertEquals(1L, actualBasketResponseDto.getProductsInBasket().get(0).getProductResponseDto().getId());
        assertEquals(1, actualBasketResponseDto.getProductsInBasket().get(0).getQuantity());
        assertEquals(false, actualBasketResponseDto.getProductsInBasket().get(0).getInstallation());
    }
}

