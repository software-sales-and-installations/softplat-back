package ru.yandex.workshop.main.controller.product;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import ru.yandex.workshop.main.controller.CrudOperations;
import ru.yandex.workshop.main.dto.product.ProductResponseDto;
import ru.yandex.workshop.main.dto.product.ProductsSearchRequestDto;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@Sql("/data-test.sql")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class PublicProductControllerTest extends CrudOperations {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    private ProductResponseDto productResponseDto1;
    private ProductResponseDto productResponseDto2;
    private ProductResponseDto productResponseDto3;

    @BeforeEach
    @SneakyThrows
    void init() {
        productResponseDto1 = getProductResponseDto(1L);
        productResponseDto2 = getProductResponseDto(2L);
        productResponseDto3 = getProductResponseDto(3L);
    }

    @Test
    @SneakyThrows
    void whenSearchCategories1And2_thenReturnAllProducts() {
        String sort = "NEWEST";
        ProductsSearchRequestDto productFilter = new ProductsSearchRequestDto();
        productFilter.setCategories(List.of(1L, 2L));

        List<ProductResponseDto> actual = getSearchResultsByFilter(productFilter, sort);
        List<ProductResponseDto> expect = List.of(productResponseDto1, productResponseDto2, productResponseDto3);
        assertEquals(expect.size(), actual.size());
        assertEquals(expect.get(0).getName(), actual.get(0).getName());
        assertEquals(expect.get(1).getName(), actual.get(1).getName());
        assertEquals(expect.get(2).getName(), actual.get(2).getName());
    }

    @Test
    @SneakyThrows
    void whenSearchVendorIds1And2_thenReturnProducts1And2() {
        String sort = "NEWEST";
        ProductsSearchRequestDto productFilter = new ProductsSearchRequestDto();
        productFilter.setVendorIds(List.of(1L, 2L));

        List<ProductResponseDto> actual = getSearchResultsByFilter(productFilter, sort);
        List<ProductResponseDto> expect = List.of(productResponseDto1, productResponseDto2);
        assertEquals(expect.size(), actual.size());
        assertEquals(expect.get(0).getName(), actual.get(0).getName());
        assertEquals(expect.get(1).getName(), actual.get(1).getName());
    }

    @Test
    @SneakyThrows
    void whenSearchTextDescriptionAndDetails_thenReturnProduct3() {
        String sort = "NEWEST";
        ProductsSearchRequestDto productFilter = new ProductsSearchRequestDto();
        productFilter.setText("description and details");

        List<ProductResponseDto> actual = getSearchResultsByFilter(productFilter, sort);
        List<ProductResponseDto> expect = List.of(productResponseDto3);
        assertEquals(expect.size(), actual.size());
        assertEquals(expect.get(0).getName(), actual.get(0).getName());
    }

    @Test
    @SneakyThrows
    void whenSearchCategoryId2AndVendorId2_thenReturnProduct2() {
        String sort = "NEWEST";
        ProductsSearchRequestDto productFilter = new ProductsSearchRequestDto();
        productFilter.setCategories(List.of(2L));
        productFilter.setVendorIds(List.of(2L));

        List<ProductResponseDto> actual = getSearchResultsByFilter(productFilter, sort);
        List<ProductResponseDto> expect = List.of(productResponseDto2);
        assertEquals(expect.size(), actual.size());
        assertEquals(expect.get(0).getName(), actual.get(0).getName());
    }

    @Test
    @SneakyThrows
    void whenSearchSellerId1AndSellerId3AndTextProduct_thenReturnProducts1And3() {
        String sort = "NEWEST";
        ProductsSearchRequestDto productFilter = new ProductsSearchRequestDto();
        productFilter.setSellerIds(List.of(1L, 3L));
        productFilter.setText("pRoDucT");

        List<ProductResponseDto> actual = getSearchResultsByFilter(productFilter, sort);
        List<ProductResponseDto> expect = List.of(productResponseDto1, productResponseDto3);
        assertEquals(expect.size(), actual.size());
        assertEquals(expect.get(0).getName(), actual.get(0).getName());
        assertEquals(expect.get(1).getName(), actual.get(1).getName());
    }

    @Test
    @SneakyThrows
    void whenSearchCountryIsRussian_thenReturnProduct3() {
        String sort = "NEWEST";
        ProductsSearchRequestDto productFilter = new ProductsSearchRequestDto();
        productFilter.setIsRussian(true);

        List<ProductResponseDto> actual = getSearchResultsByFilter(productFilter, sort);
        List<ProductResponseDto> expect = List.of(productResponseDto3);
        assertEquals(expect.size(), actual.size());
        assertEquals(expect.get(0).getName(), actual.get(0).getName());
    }

    @Test
    @SneakyThrows
    void whenSearchCountryIsNotRussian_thenReturnProducts1And2() {
        String sort = "NEWEST";
        ProductsSearchRequestDto productFilter = new ProductsSearchRequestDto();
        productFilter.setIsRussian(false);

        List<ProductResponseDto> actual = getSearchResultsByFilter(productFilter, sort);
        List<ProductResponseDto> expect = List.of(productResponseDto1, productResponseDto2);
        assertEquals(expect.size(), actual.size());
        assertEquals(expect.get(0).getName(), actual.get(0).getName());
        assertEquals(expect.get(1).getName(), actual.get(1).getName());
    }

    @Test
    @SneakyThrows
    void whenSearchWithEmptyProductFilter_thenReturnAllProducts() {
        String sort = "NEWEST";
        ProductsSearchRequestDto productFilter = new ProductsSearchRequestDto();

        List<ProductResponseDto> actual = getSearchResultsByFilter(productFilter, sort);
        List<ProductResponseDto> expect = List.of(productResponseDto1, productResponseDto2, productResponseDto3);
        assertEquals(expect.size(), actual.size());
        assertEquals(expect.get(0).getName(), actual.get(0).getName());
        assertEquals(expect.get(1).getName(), actual.get(1).getName());
        assertEquals(expect.get(2).getName(), actual.get(2).getName());
    }

    @Test
    @SneakyThrows
    void whenSearchProductsMatchingNoCriteria_thenReturnEmptyList() {
        String sort = "NEWEST";
        ProductsSearchRequestDto productFilter = new ProductsSearchRequestDto();
        productFilter.setText("foo");
        productFilter.setSellerIds(List.of(3L));
        productFilter.setVendorIds(List.of(4L));
        productFilter.setCategories(List.of(5L));
        productFilter.setIsRussian(true);

        List<ProductResponseDto> actual = getSearchResultsByFilter(productFilter, sort);
        List<ProductResponseDto> expect = Lists.emptyList();
        assertEquals(actual, expect);
    }

    @Test
    @SneakyThrows
    void whenSearchSortingByPrice_thenReturnProductsFromLowerToHigherPrice() {
        String sort = "BY_PRICE";
        ProductsSearchRequestDto productFilter = new ProductsSearchRequestDto();

        List<ProductResponseDto> actual = getSearchResultsByFilter(productFilter, sort);
        List<ProductResponseDto> expect = List.of(productResponseDto3, productResponseDto1, productResponseDto2);
        assertEquals(expect.size(), actual.size());
        assertEquals(expect.get(0).getName(), actual.get(0).getName());
        assertEquals(expect.get(1).getName(), actual.get(1).getName());
        assertEquals(expect.get(2).getName(), actual.get(2).getName());
    }

    @Test
    @SneakyThrows
    void whenSearchPriceFrom1000To2000_thenReturnProducts1And2() {
        String sort = "BY_PRICE";
        ProductsSearchRequestDto productFilter = new ProductsSearchRequestDto();
        productFilter.setPriceMin(1000F);
        productFilter.setPriceMax(2000F);

        List<ProductResponseDto> actual = getSearchResultsByFilter(productFilter, sort);
        List<ProductResponseDto> expect = List.of(productResponseDto1, productResponseDto2);
        assertEquals(expect.size(), actual.size());
        assertEquals(expect.get(0).getName(), actual.get(0).getName());
        assertEquals(expect.get(1).getName(), actual.get(1).getName());
    }

    @Test
    @SneakyThrows
    void whenSearchPriceLowerThan450_thenReturnEmptyList() {
        String sort = "NEWEST";
        ProductsSearchRequestDto productFilter = new ProductsSearchRequestDto();
        productFilter.setPriceMax(450F);

        List<ProductResponseDto> actual = getSearchResultsByFilter(productFilter, sort);
        List<ProductResponseDto> expect = Lists.emptyList();
        assertEquals(actual, expect);
    }

    @Test
    @SneakyThrows
    void whenSearchGreaterThanOrEqualTo2000_thenReturnProduct2() {
        String sort = "NEWEST";
        ProductsSearchRequestDto productFilter = new ProductsSearchRequestDto();
        productFilter.setPriceMin(2000F);

        List<ProductResponseDto> actual = getSearchResultsByFilter(productFilter, sort);
        List<ProductResponseDto> expect = List.of(productResponseDto2);
        assertEquals(actual.get(0).getName(), expect.get(0).getName());
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
