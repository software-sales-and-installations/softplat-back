package ru.yandex.workshop.main.controller.product;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.workshop.main.controller.CrudOperations;
import ru.yandex.workshop.main.exception.EntityNotFoundException;
import ru.yandex.workshop.main.dto.product.ProductResponseDto;
import ru.yandex.workshop.main.model.product.ProductStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@Sql("/data-test.sql")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class AdminProductControllerTest extends CrudOperations {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    private ProductResponseDto productResponseDto;

    @BeforeEach
    @SneakyThrows
    void init() {
        productResponseDto = getProductResponseDto(4L);
    }

    @Test
    @SneakyThrows
    @WithMockUser(authorities = {"admin:write"})
    void updateStatusProductAdmin_whenPublished_returnUpdatedDtoWithPublishedStatus() {
        long productId = productResponseDto.getId();
        ProductStatus statusUpdate = ProductStatus.PUBLISHED;
        ProductResponseDto updatedProductResponseDto = updateProductStatusByAdmin(productId, statusUpdate);

        assertEquals(productResponseDto.getId(), updatedProductResponseDto.getId());
        assertEquals(productResponseDto.getName(), updatedProductResponseDto.getName());
        assertEquals(productResponseDto.getDescription(), updatedProductResponseDto.getDescription());
        assertEquals(statusUpdate, updatedProductResponseDto.getProductStatus());
    }

    @Test
    @SneakyThrows
    @WithMockUser(authorities = {"admin:write"})
    void updateStatusProductAdmin_whenRejected_returnUpdatedDtoWithRejectedStatus() {
        long productId = productResponseDto.getId();
        ProductStatus statusUpdate = ProductStatus.REJECTED;
        ProductResponseDto updatedProductResponseDto = updateProductStatusByAdmin(productId, statusUpdate);

        assertEquals(productResponseDto.getId(), updatedProductResponseDto.getId());
        assertEquals(productResponseDto.getName(), updatedProductResponseDto.getName());
        assertEquals(productResponseDto.getDescription(), updatedProductResponseDto.getDescription());
        assertEquals(statusUpdate, updatedProductResponseDto.getProductStatus());
    }

    @Test
    @SneakyThrows
    @WithMockUser(authorities = {"admin:write"})
    void deleteProductAdmin_whenOk_deleteProduct() {
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

