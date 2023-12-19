package ru.softplat.main.server.web.controller.basket;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import ru.softplat.main.dto.basket.BasketCreateDto;
import ru.softplat.main.dto.basket.BasketPositionCreateDto;
import ru.softplat.main.dto.basket.BasketResponseDto;
import ru.softplat.main.server.web.controller.AbstractControllerTest;

import java.util.ArrayList;
import java.util.List;

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
    void getBasket_shouldReturnNewBasketResponse_whenEmptyBasket() {
        //when
        BasketResponseDto actualBasketResponseDto = getBasket();

        //then
        assertEquals(1L, actualBasketResponseDto.getBuyerId());
        assertEquals(0, actualBasketResponseDto.getProductsInBasket().size());
    }

    @Test
    @SneakyThrows
    void saveBasket_shouldReturnBasketResponseDtoWithSamePositions_whenEmptyBasket() {
        //given
        List<BasketPositionCreateDto> basketPositions = new ArrayList<>();
        basketPositions.add(new BasketPositionCreateDto(1L, 2, false));
        basketPositions.add(new BasketPositionCreateDto(5L, 7, true));
        BasketCreateDto basketToCreate = new BasketCreateDto(basketPositions);

        //when
        BasketResponseDto actualBasketResponseDto = saveBasket(1L, basketToCreate);

        //then
        assertEquals(1L, actualBasketResponseDto.getId());
        assertEquals(1L, actualBasketResponseDto.getBuyerId());
        assertEquals(2, actualBasketResponseDto.getProductsInBasket().size());
        assertEquals(1L, actualBasketResponseDto.getProductsInBasket().get(0).getProductResponseDto().getId());
        assertEquals(2, actualBasketResponseDto.getProductsInBasket().get(0).getQuantity());
        assertEquals(false, actualBasketResponseDto.getProductsInBasket().get(0).getInstallation());
        assertEquals(5L, actualBasketResponseDto.getProductsInBasket().get(1).getProductResponseDto().getId());
        assertEquals(7, actualBasketResponseDto.getProductsInBasket().get(1).getQuantity());
        assertEquals(true, actualBasketResponseDto.getProductsInBasket().get(1).getInstallation());
    }

    @Test
    @SneakyThrows
    void saveBasket_shouldReturnBasketResponseDtoWithSumOfPositions_whenNotEmptyBasket() {
        //given
        addProductInBasket(1L, false);
        addProductInBasket(5L, true);
        List<BasketPositionCreateDto> newBasketPositions = new ArrayList<>();
        newBasketPositions.add(new BasketPositionCreateDto(1L, 2, false));
        newBasketPositions.add(new BasketPositionCreateDto(5L, 7, false));
        BasketCreateDto basketToCreate = new BasketCreateDto(newBasketPositions);

        //when
        BasketResponseDto actualBasketResponseDto = saveBasket(1L, basketToCreate);

        //then
        assertEquals(1L, actualBasketResponseDto.getId());
        assertEquals(1L, actualBasketResponseDto.getBuyerId());
        assertEquals(3, actualBasketResponseDto.getProductsInBasket().size());
        assertEquals(1L, actualBasketResponseDto.getProductsInBasket().get(0).getProductResponseDto().getId());
        assertEquals(3, actualBasketResponseDto.getProductsInBasket().get(0).getQuantity());
        assertEquals(false, actualBasketResponseDto.getProductsInBasket().get(0).getInstallation());
        assertEquals(5L, actualBasketResponseDto.getProductsInBasket().get(1).getProductResponseDto().getId());
        assertEquals(1, actualBasketResponseDto.getProductsInBasket().get(1).getQuantity());
        assertEquals(true, actualBasketResponseDto.getProductsInBasket().get(1).getInstallation());
        assertEquals(5L, actualBasketResponseDto.getProductsInBasket().get(2).getProductResponseDto().getId());
        assertEquals(7, actualBasketResponseDto.getProductsInBasket().get(2).getQuantity());
        assertEquals(false, actualBasketResponseDto.getProductsInBasket().get(2).getInstallation());
    }

    @Test
    @SneakyThrows
    void addProductInBasket_shouldReturnBasketResponseDtoWithOneProduct_whenEmptyBasket() {
        //when
        BasketResponseDto actualBasketResponseDto = addProductInBasket(1L, false);

        //then
        assertEquals(1L, actualBasketResponseDto.getId());
        assertEquals(1L, actualBasketResponseDto.getBuyerId());
        assertEquals(1, actualBasketResponseDto.getProductsInBasket().size());
        assertEquals(1L, actualBasketResponseDto.getProductsInBasket().get(0).getProductResponseDto().getId());
        assertEquals(1, actualBasketResponseDto.getProductsInBasket().get(0).getQuantity());
        assertEquals(false, actualBasketResponseDto.getProductsInBasket().get(0).getInstallation());
    }

    @Test
    @SneakyThrows
    void addProductInBasket_shouldReturnBasketResponseDtoWithOnePosition_whenHaveOneProduct() {
        //given
        addProductInBasket(1L, false);

        //when
        BasketResponseDto actualBasketResponseDto = addProductInBasket(2L, false);


        //then
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
    void deleteProductFromBasket_shouldReturnEmptyBasket_whenOneProduct() {
        //given
        addProductInBasket(1L, false);

        //when
        MvcResult result = mockMvc.perform(delete("/basket/product/{productId}", 1L)
                        .header("X-Sharer-User-Id", 1L)
                        .param("installation", "false"))
                .andExpect(status().isOk())
                .andReturn();
        BasketResponseDto basketResponseDto = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                BasketResponseDto.class);

        //then
        assertEquals(1L, basketResponseDto.getBuyerId());
        assertEquals(0, basketResponseDto.getProductsInBasket().size());
    }

    @Test
    @SneakyThrows
    void deleteProductFromBasket_shouldReturnOnePosition_whenTwoProducts() {
        //given
        addProductInBasket(1L, false);
        addProductInBasket(1L, false);

        //when
        MvcResult result = mockMvc.perform(delete("/basket/product/{productId}", 1L)
                        .param("installation", "false")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andReturn();
        BasketResponseDto actualBasketResponseDto = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                BasketResponseDto.class);

        //then
        assertEquals(1L, actualBasketResponseDto.getBuyerId());
        assertEquals(1, actualBasketResponseDto.getProductsInBasket().size());
        assertEquals(1L, actualBasketResponseDto.getProductsInBasket().get(0).getProductResponseDto().getId());
        assertEquals(1, actualBasketResponseDto.getProductsInBasket().get(0).getQuantity());
        assertEquals(false, actualBasketResponseDto.getProductsInBasket().get(0).getInstallation());
    }
}

