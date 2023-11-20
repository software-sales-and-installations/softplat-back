package ru.yandex.workshop.main.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import ru.yandex.workshop.main.dto.product.ProductCreateUpdateDto;
import ru.yandex.workshop.main.dto.basket.BasketResponseDto;
import ru.yandex.workshop.main.dto.product.ProductDto;
import ru.yandex.workshop.main.dto.product.ProductResponseDto;
import ru.yandex.workshop.main.dto.user.response.BuyerResponseDto;
import ru.yandex.workshop.main.dto.user.response.SellerResponseDto;
import ru.yandex.workshop.main.dto.vendor.VendorCreateUpdateDto;
import ru.yandex.workshop.main.dto.vendor.VendorResponseDto;
import ru.yandex.workshop.main.model.product.ProductStatus;
import ru.yandex.workshop.security.dto.UserCreateDto;

import java.nio.charset.StandardCharsets;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CrudOperations {

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
}
