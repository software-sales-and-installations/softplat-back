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
import org.springframework.test.web.servlet.MvcResult;
import ru.yandex.workshop.main.controller.CrudOperations;
import ru.yandex.workshop.main.dto.product.ProductDto;
import ru.yandex.workshop.main.dto.product.ProductResponseDto;
import ru.yandex.workshop.main.model.product.License;
import ru.yandex.workshop.main.model.product.ProductStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Sql("/data-test.sql")
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class SellerProductControllerTest extends CrudOperations {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    private ProductDto productDto;

    @BeforeEach
    void init() {
        long vendorId = 1L;
        long categoryId = 1L;

        productDto = ProductDto.builder()
                .name("Product name")
                .description("Description product")
                .version("2.0.0.1")
                .category(categoryId)
                .license(License.LICENSE)
                .vendor(vendorId)
                .price(1000.421F)
                .quantity(5)
                .installation(true)
                .installationPrice(10.00F)
                .build();
    }

    @Test
    @SneakyThrows
    @WithMockUser(username = "seller1@email.ru", authorities = {"seller:write"})
    void createProduct_whenValid_returnProductResponseDto() {
        ProductResponseDto productResponseDto = createProduct(productDto);

        assertEquals(Long.class, productResponseDto.getId().getClass());
        assertEquals(productDto.getName(), productResponseDto.getName());
        assertEquals(productDto.getDescription(), productResponseDto.getDescription());
        assertEquals(productDto.getPrice(), productResponseDto.getPrice());
    }

    @Test
    @SneakyThrows
    @WithMockUser(username = "seller1@email.ru", authorities = {"seller:write"})
    void updateProductStatusBySeller_whenSent_returnProductResponseDtoWithStatusShipped() {
        ProductResponseDto createdProductDto = createProduct(productDto);
        ProductResponseDto updatedProductDto = updateProductStatusBySeller(createdProductDto.getId());

        assertEquals(ProductStatus.SHIPPED, updatedProductDto.getProductStatus());
    }

    @Test
    @SneakyThrows
    @WithMockUser(username = "seller1@email.ru", authorities = {"seller:write"})
    void updateProductBySeller_whenValid_returnNewProductResponseDto() {
        ProductResponseDto createdProductDto = createProduct(productDto);
        ProductDto updateRequestDto = ProductDto.builder()
                .name("new name")
                .description("new description")
                .build();

        long productId = createdProductDto.getId();
        ProductResponseDto updatedProductResponseDto = updateProduct(updateRequestDto, productId);

        assertEquals(updatedProductResponseDto.getName(), updateRequestDto.getName());
        assertEquals(updatedProductResponseDto.getDescription(), updateRequestDto.getDescription());
        assertEquals(updatedProductResponseDto.getId(), createdProductDto.getId());
        assertEquals(updatedProductResponseDto.getSeller().getId(), createdProductDto.getSeller().getId());
        assertEquals(updatedProductResponseDto.getVendor().getId(), createdProductDto.getVendor().getId());
        assertEquals(updatedProductResponseDto.getCategory().getId(), createdProductDto.getCategory().getId());
    }

    @SneakyThrows
    private ProductResponseDto updateProduct(ProductDto updateProductDto, long productId) {
        MvcResult result = mockMvc.perform(patch("/product/{productId}/update", productId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateProductDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(updateProductDto.getName()))
                .andExpect(jsonPath("$.description").value(updateProductDto.getDescription()))
                .andReturn();

        return objectMapper.readValue(
                result.getResponse().getContentAsString(),
                ProductResponseDto.class
        );
    }
}
