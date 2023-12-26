package ru.softplat.main.server.web.controller.product;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.assertj.core.util.Lists;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import ru.softplat.main.dto.product.ProductResponseDto;
import ru.softplat.main.dto.product.ProductStatus;
import ru.softplat.main.dto.product.ProductsListResponseDto;
import ru.softplat.main.server.web.controller.AbstractControllerTest;

import java.util.List;
import java.util.stream.Stream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@Sql("/data-test.sql")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class UserProductControllerParametrizedTest extends AbstractControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    static Stream<Arguments> getProductsAdminTestArguments() {
        return Stream.of(
                Arguments.of("DRAFT", Lists.emptyList()),
                Arguments.of("SHIPPED", List.of(4L)),
                Arguments.of("PUBLISHED", List.of(1L, 2L, 3L, 5L))
        );
    }

    static Stream<Arguments> getProductsSellerTestArguments() {
        return Stream.of(
                Arguments.of("DRAFT", Lists.emptyList()),
                Arguments.of("SHIPPED", List.of(4L)),
                Arguments.of("PUBLISHED", List.of(1L, 5L))
        );
    }

    @ParameterizedTest
    @MethodSource("getProductsAdminTestArguments")
    @SneakyThrows
    void getProductsAdmin_shouldReturnProducts(ProductStatus status, List<Long> productIds) {

        List<ProductResponseDto> actual = getAllProductsAdmin(status).getProducts();
        List<ProductResponseDto> expect = getProductsByIds(productIds);

        performAssertions(expect, actual);
    }

    @ParameterizedTest
    @MethodSource("getProductsSellerTestArguments")
    @SneakyThrows
    void getProductsSeller_shouldReturnProducts(ProductStatus status, List<Long> productIds) {

        List<ProductResponseDto> actual = getAllProductsSeller(status).getProducts();
        List<ProductResponseDto> expect = getProductsByIds(productIds);

        performAssertions(expect, actual);
    }

    @SneakyThrows
    private ProductsListResponseDto getAllProductsAdmin(ProductStatus status) {
        MvcResult result = mockMvc.perform(get("/product/admin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("minId", "0")
                        .param("pageSize", "20")
                        .param("status", String.valueOf(status)))
                .andExpect(status().isOk())
                .andReturn();

        return objectMapper.readValue(
                result.getResponse().getContentAsString(),
                ProductsListResponseDto.class);
    }

    @SneakyThrows
    private ProductsListResponseDto getAllProductsSeller(ProductStatus status) {
        MvcResult result = mockMvc.perform(get("/product/seller")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L)
                        .param("minId", "0")
                        .param("pageSize", "20")
                        .param("status", String.valueOf(status)))
                .andExpect(status().isOk())
                .andReturn();

        return objectMapper.readValue(
                result.getResponse().getContentAsString(),
                ProductsListResponseDto.class);
    }
}
