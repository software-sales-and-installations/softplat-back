package ru.yandex.workshop.main.controller.product;

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
import ru.yandex.workshop.main.controller.AbstractControllerTest;
import ru.yandex.workshop.main.dto.product.ProductResponseDto;
import ru.yandex.workshop.main.dto.product.ProductsSearchRequestDto;
import ru.yandex.workshop.main.model.vendor.Country;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@Sql("/data-test.sql")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class PublicProductControllerTest extends AbstractControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    static ProductsSearchRequestDto filter1 = ProductsSearchRequestDto.builder()
            .categories(List.of(1L, 2L))
            .build();
    static ProductsSearchRequestDto filter2 = ProductsSearchRequestDto.builder()
            .vendorIds(List.of(1L, 2L))
            .build();
    static ProductsSearchRequestDto filter3 = ProductsSearchRequestDto.builder()
            .text("description and details")
            .build();
    static ProductsSearchRequestDto filter4 = ProductsSearchRequestDto.builder()
            .categories(List.of(2L))
            .vendorIds(List.of(2L))
            .build();
    static ProductsSearchRequestDto filter5 = ProductsSearchRequestDto.builder()
            .sellerIds(List.of(1L, 3L))
            .text("pRoDucT")
            .build();
    static ProductsSearchRequestDto filter6 = ProductsSearchRequestDto.builder()
            .countries(List.of(Country.RUSSIA))
            .build();
    static ProductsSearchRequestDto filter7 = ProductsSearchRequestDto.builder()
            .countries(List.of(Country.USA))
            .build();
    static ProductsSearchRequestDto filter8 = ProductsSearchRequestDto.builder()
            .build();
    static ProductsSearchRequestDto filter9 = ProductsSearchRequestDto.builder()
            .text("foo")
            .sellerIds(List.of(3L))
            .vendorIds(List.of(4L))
            .categories(List.of(5L))
            .countries(List.of(Country.RUSSIA))
            .build();

    static ProductsSearchRequestDto filter10 = ProductsSearchRequestDto.builder()
            .build();

    static ProductsSearchRequestDto filter11 = ProductsSearchRequestDto.builder()
            .priceMin(1000F)
            .priceMax(2000F)
            .build();

    static ProductsSearchRequestDto filter12 = ProductsSearchRequestDto.builder()
            .priceMax(450F)
            .build();

    static ProductsSearchRequestDto filter13 = ProductsSearchRequestDto.builder()
            .priceMin(2000F)
            .build();

    static Stream<Arguments> productSearchTestArguments() {
        return Stream.of(
                Arguments.of(filter1, "NEWEST", List.of(1L, 2L, 3L)),
                Arguments.of(filter2, "NEWEST", List.of(1L, 2L)),
                Arguments.of(filter3, "NEWEST", List.of(3L)),
                Arguments.of(filter4, "NEWEST", List.of(2L)),
                Arguments.of(filter5, "NEWEST", List.of(1L, 3L, 5L)),
                Arguments.of(filter6, "NEWEST", List.of(3L, 5L)),
                Arguments.of(filter7, "NEWEST", List.of(1L, 2L)),
                Arguments.of(filter8, "NEWEST", List.of(1L, 2L, 3L, 5L)),
                Arguments.of(filter9, "NEWEST", Lists.emptyList()),
                Arguments.of(filter10, "BY_PRICE", List.of(3L, 1L, 2L, 5L)),
                Arguments.of(filter11, "BY_PRICE", List.of(1L, 2L)),
                Arguments.of(filter12, "BY_PRICE", Lists.emptyList()),
                Arguments.of(filter13, "NEWEST", List.of(2L, 5L))
        );
    }

    @ParameterizedTest
    @MethodSource("productSearchTestArguments")
    @SneakyThrows
    void searchProducts_shouldReturnProducts_whenProductSearchRequestFilterPasses(
            ProductsSearchRequestDto productFiler,
            String sort,
            List<Long> productIds) {

        List<ProductResponseDto> actual = getSearchResultsByFilter(productFiler, sort);
        List<ProductResponseDto> expect = getProductsByIds(productIds);

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

    private List<ProductResponseDto> getProductsByIds(List<Long> productIds) {
        List<ProductResponseDto> response = new ArrayList<>();
        for (Long id : productIds) response.add(getProductResponseDto(id));
        return response;
    }

    private List<ProductResponseDto> getSearchResultsByFilter(ProductsSearchRequestDto productFilter, String sort) throws Exception {
        MvcResult result = mockMvc.perform(get("/product/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productFilter))
                        .param("minId", "0")
                        .param("pageSize", "20")
                        .param("sort", sort))
                .andExpect(status().isOk())
                .andReturn();

        return List.of(objectMapper.readValue(
                result.getResponse().getContentAsString(),
                ProductResponseDto[].class));
    }
}
