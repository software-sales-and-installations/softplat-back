package ru.yandex.workshop.main.controller.product;

import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.workshop.main.controller.AbstractControllerTest;
import ru.yandex.workshop.main.exception.EntityNotFoundException;
import ru.yandex.workshop.main.dto.product.ProductResponseDto;
import ru.yandex.workshop.main.model.product.ProductStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@Sql("/data-test.sql")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class AdminProductControllerTest extends AbstractControllerTest {

    @Autowired
    private MockMvc mockMvc;
    private ProductResponseDto productResponseDto;

    @BeforeEach
    @SneakyThrows
    void init() {
        productResponseDto = getProductResponseDto(4L);
    }

    @Test
    @SneakyThrows
    @WithMockUser(authorities = {"admin:write"})
    void updateStatusProductAdmin_shouldReturnUpdatedDtoWithPublishedStatus_whenPublished() {
        long productId = productResponseDto.getId();
        ProductStatus statusUpdate = ProductStatus.PUBLISHED;
        ProductResponseDto actualProductResponseDto = updateProductStatusByAdmin(productId, statusUpdate);

        assertEquals(productResponseDto.getId(), actualProductResponseDto.getId());
        assertEquals(productResponseDto.getName(), actualProductResponseDto.getName());
        assertEquals(productResponseDto.getDescription(), actualProductResponseDto.getDescription());
        assertEquals(statusUpdate, actualProductResponseDto.getProductStatus());
    }

    @Test
    @SneakyThrows
    @WithMockUser(authorities = {"admin:write"})
    void updateStatusProductAdmin_shouldReturnUpdatedDtoWithRejectedStatus_whenRejected() {
        long productId = productResponseDto.getId();
        ProductStatus statusUpdate = ProductStatus.REJECTED;
        ProductResponseDto actualProductResponseDto = updateProductStatusByAdmin(productId, statusUpdate);

        assertEquals(productResponseDto.getId(), actualProductResponseDto.getId());
        assertEquals(productResponseDto.getName(), actualProductResponseDto.getName());
        assertEquals(productResponseDto.getDescription(), actualProductResponseDto.getDescription());
        assertEquals(statusUpdate, actualProductResponseDto.getProductStatus());
    }

    @Test
    @SneakyThrows
    @WithMockUser(authorities = {"admin:write"})
    void deleteProductAdmin_shouldDeleteProduct() {
        long productId = productResponseDto.getId();

        mockMvc.perform(delete("/product/{productId}", productId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/product/{productId}", productId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(result -> assertTrue(result.getResolvedException()
                        instanceof EntityNotFoundException));
    }
}

