package ru.softplat.main.server.web.controller.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import ru.softplat.main.dto.product.ProductResponseDto;
import ru.softplat.main.dto.product.ProductsListResponseDto;
import ru.softplat.main.server.web.controller.AbstractControllerTest;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Sql("/data-test.sql")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class BuyerRecommendationsTest extends AbstractControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @SneakyThrows
    void getRecommendations_shouldReturnProducts3And5_whenBuyer1() {
        // given
        long buyerId = 1L;

        // then
        List<ProductResponseDto> actual = getRecommendations(buyerId).getProducts();
        List<ProductResponseDto> expect = getProductsByIds(List.of(3L, 5L));

        performAssertions(expect, actual);
    }

    @Test
    @SneakyThrows
    void getRecommendations_shouldReturnProduct1_whenBuyer2() {
        // given
        long buyerId = 2L;

        // then
        List<ProductResponseDto> actual = getRecommendations(buyerId).getProducts();
        List<ProductResponseDto> expect = getProductsByIds(List.of(1L));

        performAssertions(expect, actual);
    }

    @Test
    @SneakyThrows
    void getRecommendations_shouldReturnProduct2_whenBuyer3() {
        // given
        long buyerId = 3L;

        // then
        List<ProductResponseDto> actual = getRecommendations(buyerId).getProducts();
        List<ProductResponseDto> expect = getProductsByIds(List.of(2L));

        performAssertions(expect, actual);
    }

    @SneakyThrows
    private ProductsListResponseDto getRecommendations(long userId) {
        MvcResult result = mockMvc.perform(get("/buyer/recommendations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("minId", "0")
                        .param("pageSize", "5")
                        .header("X-Sharer-User-Id", String.valueOf(userId)))
                .andExpect(status().isOk())
                .andReturn();

        return objectMapper.readValue(
                result.getResponse().getContentAsString(),
                ProductsListResponseDto.class);
    }
}