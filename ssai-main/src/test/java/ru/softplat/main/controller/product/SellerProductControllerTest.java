package ru.softplat.main.controller.product;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.test.web.servlet.MvcResult;
import ru.yandex.workshop.main.controller.AbstractControllerTest;
import ru.yandex.workshop.main.dto.product.ProductCreateUpdateDto;
import ru.yandex.workshop.main.dto.product.ProductResponseDto;
import ru.yandex.workshop.main.model.product.License;
import ru.yandex.workshop.main.model.product.ProductStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@Sql("/data-test.sql")
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class SellerProductControllerTest extends AbstractControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    private ProductCreateUpdateDto productCreateUpdateDto;

    @BeforeEach
    void init() {
        long vendorId = 1L;
        long categoryId = 1L;

        productCreateUpdateDto = ProductCreateUpdateDto.builder()
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
    void createProduct_shouldReturnProduct_whenRequestDtoIsValid() {
        ProductResponseDto productResponseDto = createProduct(productCreateUpdateDto);

        assertEquals(Long.class, productResponseDto.getId().getClass());
        assertEquals(productCreateUpdateDto.getName(), productResponseDto.getName());
        assertEquals(productCreateUpdateDto.getDescription(), productResponseDto.getDescription());
        assertEquals(productCreateUpdateDto.getPrice(), productResponseDto.getPrice());
    }

    @Test
    @SneakyThrows
    @WithMockUser(username = "seller1@email.ru", authorities = {"seller:write"})
    void updateProductStatusBySeller_shouldReturnProductResponseDtoWithStatusShipped() {
        ProductResponseDto createdProductDto = createProduct(productCreateUpdateDto);
        ProductResponseDto updatedProductDto = updateProductStatusBySeller(createdProductDto.getId());

        assertEquals(ProductStatus.SHIPPED, updatedProductDto.getProductStatus());
    }

    @Test
    @SneakyThrows
    @WithMockUser(username = "seller1@email.ru", authorities = {"seller:write"})
    void updateProductBySeller_shouldReturnNewProductResponseDto() {
        ProductResponseDto createdProductDto = createProduct(productCreateUpdateDto);
        ProductCreateUpdateDto updateRequestDto = ProductCreateUpdateDto.builder()
                .name("new name")
                .description("new description")
                .build();

        long productId = createdProductDto.getId();
        ProductResponseDto actualProductResponseDto = updateProduct(updateRequestDto, productId);

        assertEquals(actualProductResponseDto.getName(), updateRequestDto.getName());
        assertEquals(actualProductResponseDto.getDescription(), updateRequestDto.getDescription());
        assertEquals(actualProductResponseDto.getId(), createdProductDto.getId());
        assertEquals(actualProductResponseDto.getSeller().getId(), createdProductDto.getSeller().getId());
        assertEquals(actualProductResponseDto.getVendor().getId(), createdProductDto.getVendor().getId());
        assertEquals(actualProductResponseDto.getCategory().getId(), createdProductDto.getCategory().getId());
    }

    @SneakyThrows
    private ProductResponseDto updateProduct(ProductCreateUpdateDto updateProductCreateUpdateDto, long productId) {
        MvcResult result = mockMvc.perform(patch("/product/{productId}/update", productId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateProductCreateUpdateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(updateProductCreateUpdateDto.getName()))
                .andExpect(jsonPath("$.description").value(updateProductCreateUpdateDto.getDescription()))
                .andReturn();

        return objectMapper.readValue(
                result.getResponse().getContentAsString(),
                ProductResponseDto.class
        );
    }
}
