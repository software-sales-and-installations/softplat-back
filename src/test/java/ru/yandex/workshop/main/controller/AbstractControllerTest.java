package ru.yandex.workshop.main.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import ru.yandex.workshop.main.dto.basket.BasketResponseDto;
import ru.yandex.workshop.main.dto.basket.OrderCreateDto;
import ru.yandex.workshop.main.dto.basket.OrderResponseDto;
import ru.yandex.workshop.main.dto.product.ProductCreateUpdateDto;
import ru.yandex.workshop.main.dto.product.ProductResponseDto;
import ru.yandex.workshop.main.dto.user.response.BuyerResponseDto;
import ru.yandex.workshop.main.dto.user.response.SellerResponseDto;
import ru.yandex.workshop.main.dto.vendor.VendorCreateUpdateDto;
import ru.yandex.workshop.main.dto.vendor.VendorResponseDto;
import ru.yandex.workshop.main.model.product.ProductStatus;
import ru.yandex.workshop.security.dto.UserCreateDto;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public abstract class AbstractControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @SneakyThrows
    public void createUser(UserCreateDto userDto) {
        mockMvc.perform(post("/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isOk());
    }

    @SneakyThrows
    public VendorResponseDto createVendor(VendorCreateUpdateDto vendorCreateUpdateDto) {
        MvcResult result = mockMvc.perform(post("/vendor")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(vendorCreateUpdateDto)))
                .andExpect(status().isCreated())
                .andReturn();

        return objectMapper.readValue(
                result.getResponse().getContentAsString(),
                VendorResponseDto.class);
    }

    @SneakyThrows
    public ProductResponseDto createProduct(ProductCreateUpdateDto productCreateUpdateDto) {
        MvcResult result = mockMvc.perform(post("/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productCreateUpdateDto)))
                .andExpect(status().isCreated())
                .andReturn();

        return objectMapper.readValue(
                result.getResponse().getContentAsString(),
                ProductResponseDto.class);
    }

    @SneakyThrows
    public BuyerResponseDto getBuyerResponseDto(long userId) {
        MvcResult result = mockMvc.perform(get("/buyer/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        return objectMapper.readValue(
                result.getResponse().getContentAsString(),
                BuyerResponseDto.class);
    }

    @SneakyThrows
    public ProductResponseDto getProductResponseDto(long productId) {
        MvcResult result = mockMvc.perform(get("/product/{productId}", productId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        return objectMapper.readValue(
                result.getResponse().getContentAsString(),
                ProductResponseDto.class);
    }

    @SneakyThrows
    public SellerResponseDto getSellerResponseDto(long sellerId) {
        MvcResult result = mockMvc.perform(get("/seller/{sellerId}", sellerId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        return objectMapper.readValue(
                result.getResponse().getContentAsString(),
                SellerResponseDto.class);
    }

    @SneakyThrows
    public VendorResponseDto getVendorResponseDto(long vendorId) {
        MvcResult result = mockMvc.perform(get("/vendor/{vendorId}", vendorId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        return objectMapper.readValue(
                result.getResponse().getContentAsString(),
                VendorResponseDto.class);
    }

    @SneakyThrows
    public ProductResponseDto updateProductStatusByAdmin(long productId, ProductStatus statusUpdate) {
        MvcResult result = mockMvc.perform(patch("/product/{productId}/moderation", productId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .param("status", statusUpdate.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        return objectMapper.readValue(
                result.getResponse().getContentAsString(),
                ProductResponseDto.class);
    }

    @SneakyThrows
    public ProductResponseDto updateProductStatusBySeller(long productId) {
        MvcResult result = mockMvc.perform(patch("/product/{productId}/send", productId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .param("status", ProductStatus.SHIPPED.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        return objectMapper.readValue(
                result.getResponse().getContentAsString(),
                ProductResponseDto.class);
    }

    @SneakyThrows
    public BasketResponseDto addProductInBasket(long productId, boolean installation) {
        MvcResult result = mockMvc.perform(post("/buyer/basket/{productId}", productId)
                        .param("installation", String.valueOf(installation))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        return objectMapper.readValue(
                result.getResponse().getContentAsString(),
                BasketResponseDto.class);
    }

    @SneakyThrows
    public BasketResponseDto getBasket() {
        MvcResult result = mockMvc.perform(get("/buyer/basket"))
                .andExpect(status().isOk())
                .andReturn();

        return objectMapper.readValue(
                result.getResponse().getContentAsString(),
                BasketResponseDto.class);
    }

    @SneakyThrows
    public OrderResponseDto addOrder(OrderCreateDto orderCreateDto) {
        MvcResult result = mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(orderCreateDto)))
                .andExpect(status().isCreated())
                .andReturn();

        return objectMapper.readValue(
                result.getResponse().getContentAsString(),
                OrderResponseDto.class);
    }

    @SneakyThrows
    public OrderResponseDto getOrder(long orderId) {
        MvcResult result = mockMvc.perform(get("/orders/{orderId}", orderId))
                .andExpect(status().isOk())
                .andReturn();

        return objectMapper.readValue(
                result.getResponse().getContentAsString(),
                OrderResponseDto.class);
    }

    public List<ProductResponseDto> getProductsByIds(List<Long> productIds) {
        List<ProductResponseDto> response = new ArrayList<>();
        for (Long id : productIds) response.add(getProductResponseDto(id));
        return response;
    }

    public void performAssertions(List<ProductResponseDto> actual, List<ProductResponseDto> expect) {
        assertEquals(expect.size(), actual.size());

        for (int i = 0; i < expect.size(); i++) {
            assertEquals(expect.get(i).getName(), actual.get(i).getName());
            assertEquals(expect.get(i).getDescription(), actual.get(i).getDescription());
            assertEquals(expect.get(i).getVersion(), actual.get(i).getVersion());
            assertEquals(expect.get(i).getProductionTime(), actual.get(i).getProductionTime());
            assertEquals(expect.get(i).getCategory().getId(), actual.get(i).getCategory().getId());
            assertEquals(expect.get(i).getLicense(), actual.get(i).getLicense());
            assertEquals(expect.get(i).getVendor().getId(), actual.get(i).getVendor().getId());
            assertEquals(expect.get(i).getSeller().getId(), actual.get(i).getSeller().getId());
            assertEquals(expect.get(i).getPrice(), actual.get(i).getPrice());
            assertEquals(expect.get(i).getQuantity(), actual.get(i).getQuantity());
            assertEquals(expect.get(i).getProductStatus(), actual.get(i).getProductStatus());
        }
    }
}
