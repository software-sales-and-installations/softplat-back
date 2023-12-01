package ru.yandex.workshop.main.controller.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
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
import ru.yandex.workshop.main.dto.product.ProductResponseDto;
import ru.yandex.workshop.main.dto.product.ProductsListResponseDto;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@Sql("/data-test.sql")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class BuyerRecommendationsTest extends AbstractControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @SneakyThrows
    @WithMockUser(username = "buyer1@email.ru", authorities = {"buyer:write"})
    void getRecommendations_shouldReturnProducts3And5_whenBuyer1() {
        List<ProductResponseDto> actual = getRecommendations().getProducts();
        List<ProductResponseDto> expect = getProductsByIds(List.of(3L, 5L));

        performAssertions(actual, expect);
    }

    @Test
    @SneakyThrows
    @WithMockUser(username = "buyer2@email.ru", authorities = {"buyer:write"})
    void getRecommendations_shouldReturnProduct1_whenBuyer2() {
        List<ProductResponseDto> actual = getRecommendations().getProducts();
        List<ProductResponseDto> expect = getProductsByIds(List.of(1L));

        performAssertions(actual, expect);
    }

    @Test
    @SneakyThrows
    @WithMockUser(username = "buyer3@email.ru", authorities = {"buyer:write"})
    void getRecommendations_shouldReturnProduct2_whenBuyer3() {
        List<ProductResponseDto> actual = getRecommendations().getProducts();
        List<ProductResponseDto> expect = getProductsByIds(List.of(2L));

        performAssertions(actual, expect);
    }

    @SneakyThrows
    private ProductsListResponseDto getRecommendations() {
        MvcResult result = mockMvc.perform(get("/buyer/recommendations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("minId", "0")
                        .param("pageSize", "5"))
                .andExpect(status().isOk())
                .andReturn();

        return objectMapper.readValue(
                result.getResponse().getContentAsString(),
                ProductsListResponseDto.class);
    }
}
