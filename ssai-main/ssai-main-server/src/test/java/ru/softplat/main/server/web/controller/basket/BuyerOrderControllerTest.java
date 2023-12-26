package ru.softplat.main.server.web.controller.basket;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import ru.softplat.main.dto.basket.OrderCreateDto;
import ru.softplat.main.dto.basket.OrderResponseDto;
import ru.softplat.main.dto.basket.OrdersListResponseDto;
import ru.softplat.main.dto.product.ProductResponseDto;
import ru.softplat.main.server.service.buyer.OrderService;
import ru.softplat.main.server.web.controller.AbstractControllerTest;
import ru.softplat.stats.client.StatsClient;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@Sql("/data-test.sql")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class BuyerOrderControllerTest extends AbstractControllerTest {
    @InjectMocks
    private OrderService orderService;
    @Mock
    private StatsClient statsClient;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void init() {
        addProductInBasket(1L, false);
        addProductInBasket(5L, true);
        addProductInBasket(1L, false);
        addProductInBasket(2L, false);
    }

    @Test
    @Disabled
    @SneakyThrows
    void addOrder_shouldReturnOrderResponseDto_whenHaveOneId() {
        List<Long> basketPositionIds = new ArrayList<>();
        basketPositionIds.add(1L);

        OrderResponseDto orderResponseDto = addOrder(new OrderCreateDto(basketPositionIds));

        ProductResponseDto productResponseDto = getProductResponseDto(orderResponseDto.getId());

        assertEquals(1L, orderResponseDto.getBuyer().getId());
        assertEquals(1, orderResponseDto.getProductsOrdered().size());
        assertEquals(1L, orderResponseDto.getProductsOrdered().get(0).getProductResponseDto().getId());
        assertEquals(2, orderResponseDto.getProductsOrdered().get(0).getQuantity());
        assertEquals(false, orderResponseDto.getProductsOrdered().get(0).getInstallation());
        assertEquals(productResponseDto.getPrice(), orderResponseDto.getProductsOrdered().get(0).getProductCost());
        assertEquals(productResponseDto.getPrice() * 2, orderResponseDto.getOrderCost());
        assertNotNull(orderResponseDto.getProductionTime());
    }

    @Test
    @Disabled
    @SneakyThrows
    void addOrder_shouldReturnOrderResponseDto_whenHaveTwoIdsAndInstallation() {
        List<Long> basketPositionIds = new ArrayList<>();
        basketPositionIds.add(1L);
        basketPositionIds.add(2L);

        OrderResponseDto actualOrderResponseDto = addOrder(new OrderCreateDto(basketPositionIds));

        ProductResponseDto actualProductResponseDto1 = getProductResponseDto(1L);
        ProductResponseDto productResponseDto2 = getProductResponseDto(5L);

        assertEquals(1L, actualOrderResponseDto.getBuyer().getId());
        assertEquals(actualProductResponseDto1.getPrice() * 2 + productResponseDto2.getPrice() +
                productResponseDto2.getInstallationPrice(), actualOrderResponseDto.getOrderCost());
        assertNotNull(actualOrderResponseDto.getProductionTime());
        assertEquals(2, actualOrderResponseDto.getProductsOrdered().size());

        assertEquals(1L, actualOrderResponseDto.getProductsOrdered().get(0).getProductResponseDto().getId());
        assertEquals(2, actualOrderResponseDto.getProductsOrdered().get(0).getQuantity());
        assertEquals(false, actualOrderResponseDto.getProductsOrdered().get(0).getInstallation());
        assertEquals(actualProductResponseDto1.getPrice(), actualOrderResponseDto.getProductsOrdered().get(0).getProductCost());

        assertEquals(5L, actualOrderResponseDto.getProductsOrdered().get(1).getProductResponseDto().getId());
        assertEquals(1, actualOrderResponseDto.getProductsOrdered().get(1).getQuantity());
        assertEquals(true, actualOrderResponseDto.getProductsOrdered().get(1).getInstallation());
        assertEquals(productResponseDto2.getPrice() + productResponseDto2.getInstallationPrice(),
                actualOrderResponseDto.getProductsOrdered().get(1).getProductCost());
    }

    @Test
    @Disabled
    @SneakyThrows
    void getOrder_shouldReturnOrderResponseDto_whenHaveOneId() {
        List<Long> basketPositionIds = new ArrayList<>();
        basketPositionIds.add(1L);
        OrderResponseDto createdOrder = addOrder(new OrderCreateDto(basketPositionIds));

        OrderResponseDto actualOrderResponseDto = getOrder(createdOrder.getId());

        ProductResponseDto actualProductResponseDto = getProductResponseDto(1L);

        assertEquals(1L, actualOrderResponseDto.getBuyer().getId());
        assertEquals(1, actualOrderResponseDto.getProductsOrdered().size());
        assertEquals(1L, actualOrderResponseDto.getProductsOrdered().get(0).getProductResponseDto().getId());
        assertEquals(2, actualOrderResponseDto.getProductsOrdered().get(0).getQuantity());
        assertEquals(false, actualOrderResponseDto.getProductsOrdered().get(0).getInstallation());
        assertEquals(actualProductResponseDto.getPrice(), actualOrderResponseDto.getProductsOrdered().get(0).getProductCost());
        assertEquals(actualProductResponseDto.getPrice() * 2, actualOrderResponseDto.getOrderCost());
        assertNotNull(actualOrderResponseDto.getProductionTime());
    }

    @Test
    @SneakyThrows
    void getAllOrders_shouldReturnListOfOrderResponseDto_whenHaveTwoIds() {
        MvcResult result = mockMvc.perform(get("/orders")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andReturn();

        List<OrderResponseDto> orderResponseDtoList = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                OrdersListResponseDto.class).getOrders();

        assertEquals(1, orderResponseDtoList.size());
        assertEquals(1, orderResponseDtoList.get(0).getProductsOrdered().size());
    }
}
