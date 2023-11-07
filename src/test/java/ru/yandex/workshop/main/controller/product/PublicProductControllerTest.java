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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import ru.yandex.workshop.main.dto.product.ProductDto;
import ru.yandex.workshop.main.dto.product.ProductFilter;
import ru.yandex.workshop.main.dto.product.ProductResponseDto;
import ru.yandex.workshop.main.dto.seller.SellerDto;
import ru.yandex.workshop.main.dto.seller.SellerResponseDto;
import ru.yandex.workshop.main.model.product.License;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class PublicProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private ProductResponseDto productResponseDto1, productResponseDto2, productResponseDto3;

    @BeforeEach
    @SneakyThrows
    void init() {
        SellerDto sellerDto1 = SellerDto.builder().name("seller1").email("seller1@email.ru").phone("1111111111").build();
        SellerDto sellerDto2 = SellerDto.builder().name("seller2").email("seller2@email.ru").phone("2222222222").build();
        SellerDto sellerDto3 = SellerDto.builder().name("seller3").email("seller3@email.ru").phone("3333333333").build();

        SellerResponseDto sellerResponseDto1 = createSeller(sellerDto1);
        SellerResponseDto sellerResponseDto2 = createSeller(sellerDto2);
        SellerResponseDto sellerResponseDto3 = createSeller(sellerDto3);

        long categoryId1 = 1, vendorId1 = 1, sellerId1 = sellerResponseDto1.getId();
        ProductDto productDto1 = ProductDto.builder()
                .name("product1")
                .description("product1 description")
                .version("2.0.0.1")
                .category(categoryId1)
                .license(License.LICENSE)
                .vendor(vendorId1)
                .seller(sellerId1)
                .price(1000F)
                .quantity(5)
                .installation(true)
                .productAvailability(true)
                .installationPrice(10.00F)
                .build();

        long categoryId2 = 2, vendorId2 = 2, sellerId2 = sellerResponseDto2.getId();
        ProductDto productDto2 = ProductDto.builder()
                .name("product2")
                .description("product2 description")
                .version("2.0.0.1")
                .category(categoryId2)
                .license(License.LICENSE)
                .vendor(vendorId2)
                .seller(sellerId2)
                .price(2000F)
                .quantity(5)
                .installation(true)
                .productAvailability(true)
                .installationPrice(10.00F)
                .build();

        long categoryId3 = 2, vendorId3 = 3, sellerId3 = sellerResponseDto3.getId();
        ProductDto productDto3 = ProductDto.builder()
                .name("product3")
                .description("product3 description and details")
                .version("2.0.0.1")
                .category(categoryId3)
                .license(License.LICENSE)
                .vendor(vendorId3)
                .seller(sellerId3)
                .price(500F)
                .quantity(5)
                .installation(true)
                .productAvailability(true)
                .installationPrice(10.00F)
                .build();

        productResponseDto1 = createProduct(productDto1);
        productResponseDto2 = createProduct(productDto2);
        productResponseDto3 = createProduct(productDto3);

        approveProductByAdmin(productResponseDto3.getId());
        approveProductByAdmin(productResponseDto2.getId());
        approveProductByAdmin(productResponseDto1.getId());
    }

    @Test
    @SneakyThrows
    void whenSearchCategories1And2_thenReturnAllProducts() {
        String sort = "new";
        ProductFilter productFilter = new ProductFilter();
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
        String sort = "new";
        ProductFilter productFilter = new ProductFilter();
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
        String sort = "new";
        ProductFilter productFilter = new ProductFilter();
        productFilter.setText("description and details");

        List<ProductResponseDto> actual = getSearchResultsByFilter(productFilter, sort);
        List<ProductResponseDto> expect = List.of(productResponseDto3);
        assertEquals(expect.size(), actual.size());
        assertEquals(expect.get(0).getName(), actual.get(0).getName());
    }

    @Test
    @SneakyThrows
    void whenSearchCategoryId2AndVendorId2_thenReturnProduct2() {
        String sort = "new";
        ProductFilter productFilter = new ProductFilter();
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
        String sort = "new";
        ProductFilter productFilter = new ProductFilter();
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
        String sort = "new";
        ProductFilter productFilter = new ProductFilter();
        productFilter.setIsRussian(true);

        List<ProductResponseDto> actual = getSearchResultsByFilter(productFilter, sort);
        List<ProductResponseDto> expect = List.of(productResponseDto3);
        assertEquals(expect.size(), actual.size());
        assertEquals(expect.get(0).getName(), actual.get(0).getName());
    }

    @Test
    @SneakyThrows
    void whenSearchCountryIsNotRussian_thenReturnProducts1And2() {
        String sort = "new";
        ProductFilter productFilter = new ProductFilter();
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
        String sort = "new";
        ProductFilter productFilter = new ProductFilter();

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
        String sort = "new";
        ProductFilter productFilter = new ProductFilter();
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
        String sort = "price";
        ProductFilter productFilter = new ProductFilter();

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
        String sort = "price";
        ProductFilter productFilter = new ProductFilter();
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
        String sort = "new";
        ProductFilter productFilter = new ProductFilter();
        productFilter.setPriceMax(450F);

        List<ProductResponseDto> actual = getSearchResultsByFilter(productFilter, sort);
        List<ProductResponseDto> expect = Lists.emptyList();
        assertEquals(actual, expect);
    }

    @Test
    @SneakyThrows
    void whenSearchGreaterThanOrEqualTo2000_thenReturnProduct2() {
        String sort = "new";
        ProductFilter productFilter = new ProductFilter();
        productFilter.setPriceMin(2000F);

        List<ProductResponseDto> actual = getSearchResultsByFilter(productFilter, sort);
        List<ProductResponseDto> expect = List.of(productResponseDto2);
        assertEquals(actual.get(0).getName(), expect.get(0).getName());
    }

    private List<ProductResponseDto> getSearchResultsByFilter(ProductFilter productFilter, String sort) throws Exception {
        MvcResult result = mockMvc.perform(get("/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productFilter))
                        .param("from", "0")
                        .param("size", "20")
                        .param("sort", sort))
                .andExpect(status().isOk())
                .andReturn();

        return List.of(objectMapper.readValue(
                result.getResponse().getContentAsString(),
                ProductResponseDto[].class));
    }

    private SellerResponseDto createSeller(SellerDto sellerDto) throws Exception {
        MvcResult result = mockMvc.perform(post("/seller/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sellerDto)))
                .andExpect(status().isOk())
                .andReturn();

        return objectMapper.readValue(
                result.getResponse().getContentAsString(),
                SellerResponseDto.class);
    }

    private ProductResponseDto createProduct(ProductDto productDto) throws Exception {
        MvcResult result = mockMvc.perform(post("/seller/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productDto)))
                .andExpect(status().isOk())
                .andReturn();

        return objectMapper.readValue(
                result.getResponse().getContentAsString(),
                ProductResponseDto.class);
    }

    private void approveProductByAdmin(long productId) throws Exception {
        mockMvc.perform(patch("/admin/product/{productId}/published", productId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}