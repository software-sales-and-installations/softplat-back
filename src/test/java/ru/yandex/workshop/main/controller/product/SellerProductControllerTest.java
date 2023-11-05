package ru.yandex.workshop.main.controller.product;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.workshop.main.dto.product.ProductDto;
import ru.yandex.workshop.main.dto.product.ProductMapper;
import ru.yandex.workshop.main.dto.product.ProductResponseDto;
import ru.yandex.workshop.main.dto.seller.SellerMapper;
import ru.yandex.workshop.main.dto.vendor.VendorMapper;
import ru.yandex.workshop.main.model.product.Category;
import ru.yandex.workshop.main.model.product.License;
import ru.yandex.workshop.main.model.product.Product;
import ru.yandex.workshop.main.model.product.ProductStatus;
import ru.yandex.workshop.main.model.seller.BankRequisites;
import ru.yandex.workshop.main.model.seller.Seller;
import ru.yandex.workshop.main.model.vendor.Country;
import ru.yandex.workshop.main.model.vendor.Vendor;
import ru.yandex.workshop.main.service.product.ProductService;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = SellerProductController.class)
class SellerProductControllerTest {

    @MockBean
    private ProductService productService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper = new ObjectMapper();

    static Product product;
    static Long productId;
    static ProductResponseDto productResponseDto;
    static ProductDto productDto;
    static Vendor vendor;
    static Long vendorId;
    static Seller seller;
    static Long sellerId;
    static Category category;
    static Long categoryId;
    static BankRequisites bankRequisites;
    static LocalDateTime time;
    static String foramttedString;

    static List<ProductResponseDto> productDtoList;

    @BeforeAll
    static void beforeEach() {
        time = LocalDateTime.of(LocalDate.of(2023, 11, 1),
                LocalTime.of(22, 21, 41, 760048200));
        DateTimeFormatter aFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        foramttedString = time.format(aFormatter);

        bankRequisites = new BankRequisites(
                1L,
                "1111 2222 3333 4444");

        vendorId = 1L;
        vendor = Vendor.builder()
                .id(vendorId)
                .name("name1")
                .description("Name One")
                .country(Country.RUSSIA)
                .build();

        sellerId = 1L;
        seller = Seller.builder()
                .id(sellerId)
                .email("NameTwo@gmail.com")
                .name("Name")
                .phone(" +79111111111")
                .description("Description seller")
                .registrationTime(time)
                .requisites(bankRequisites)
                .build();

        categoryId = 1L;
        category = new Category(
                categoryId,
                "Category");

        productId = 1L;
        product = Product.builder()
                .id(productId)
                .name("Product name")
                .description("Description product")
                .version("2.0.0.1")
                .productionTime(time)
                .category(category)
                .license(License.LICENSE)
                .vendor(vendor)
                .seller(seller)
                .price(1000.421F)
                .quantity(5)
                .installation(true)
                .productStatus(ProductStatus.DRAFT)
                .productAvailability(true)
                .build();

        productResponseDto = ProductResponseDto.builder()
                .id(productId)
                .name("Product name")
                .description("Description product")
                .version("2.0.0.1")
                .productionTime(time)
                .category(category)
                .license(License.LICENSE)
                .vendor(VendorMapper.INSTANCE.vendorToVendorResponseDto(vendor))
                .seller(SellerMapper.INSTANCE.sellerToSellerForResponse(seller))
                .price(1000.421F)
                .quantity(5)
                .installation(true)
                .productStatus(ProductStatus.DRAFT)
                .productAvailability(true)
                .build();

        productDto = ProductDto.builder()
                .name("Product name")
                .description("Description product")
                .version("2.0.0.1")
                .category(categoryId)
                .license(License.LICENSE)
                .vendor(vendorId)
                .seller(sellerId)
                .price(1000.421F)
                .quantity(5)
                .installation(true)
                .productAvailability(true)
                .build();

        productDtoList = List.of(ProductMapper.INSTANCE.productToProductResponseDto(product));
    }

    @Test
    @DisplayName("Вызов метода getProductsSellerTest: получение всех продуктов продавца")
    void getProductsSellerTest() throws Exception {
        when(productService
                .getProductsSeller(anyLong(), anyInt(), anyInt()))
                .thenReturn(productDtoList);
        mockMvc.perform(get("/seller/1/products")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Share-Product-Id", productResponseDto.getId())
                        .param("sellerId", "1")
                        .param("from", "0")
                        .param("size", "20"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(productDtoList)));
    }

    @Test
    @DisplayName("Вызов метода getProductByIdTest: получение продуктa по id конкретного продавца")
    void getProductByIdTest() throws Exception {
        when(productService
                .getProductById(anyLong(), anyLong()))
                .thenReturn(productResponseDto);

        mockMvc.perform(get("/seller/1/product/1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .param("sellerId", "1")
                        .param("productId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(productResponseDto.getId()), Long.class))
                .andExpect(jsonPath("$.description", is(productResponseDto.getDescription().toString())))
                .andExpect(jsonPath("$.version", is(productResponseDto.getVersion().toString())))
                .andExpect(jsonPath("$.category.id", is(productResponseDto.getCategory().getId()), Long.class))
                .andExpect(jsonPath("$.category.name", is(productResponseDto.getCategory().getName().toString())))
                .andExpect(jsonPath("$.license", is(productResponseDto.getLicense().toString())))
                .andExpect(jsonPath("$.vendor.id", is(productResponseDto.getVendor().getId()), Long.class))
                .andExpect(jsonPath("$.vendor.name", is(productResponseDto.getVendor().getName().toString())))
                .andExpect(jsonPath("$.vendor.description", is(productResponseDto.getVendor().getDescription().toString())))
                .andExpect(jsonPath("$.vendor.country", is(productResponseDto.getVendor().getCountry().toString())))
                .andExpect(jsonPath("$.seller.id", is(productResponseDto.getSeller().getId()), Long.class))
                .andExpect(jsonPath("$.seller.email", is(productResponseDto.getSeller().getEmail().toString())))
                .andExpect(jsonPath("$.seller.name", is(productResponseDto.getSeller().getName().toString())))
                .andExpect(jsonPath("$.seller.phone", is(productResponseDto.getSeller().getPhone().toString())))
                .andExpect(jsonPath("$.seller.description", is(productResponseDto.getSeller().getDescription().toString())))
                .andExpect(jsonPath("$.seller.requisites.account", is(productResponseDto.getSeller().getRequisites().getAccount()), String.class))
                .andExpect(jsonPath("$.seller.requisites.account", is(productResponseDto.getSeller().getRequisites().getAccount().toString())))
                .andExpect(jsonPath("$.price", is(productResponseDto.getPrice()), Float.class))
                .andExpect(jsonPath("$.quantity", is(productResponseDto.getQuantity()), Integer.class))
                .andExpect(jsonPath("$.installation", is(productResponseDto.getInstallation()), Boolean.class))
                .andExpect(jsonPath("$.productStatus", is(productResponseDto.getProductStatus().toString())))
                .andExpect(jsonPath("$.productAvailability", is(productResponseDto.getProductAvailability()), Boolean.class));
    }

    @Test
    @DisplayName("Вызов метода createProductTest: создание продукта")
    void createProductTest() throws Exception {
        when(productService
                .createProduct(any()))
                .thenReturn(productResponseDto);
        mockMvc.perform(post("/seller/product")
                        .content(mapper.writeValueAsString(productDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(productResponseDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(productResponseDto.getName())))
                .andExpect(jsonPath("$.description", is(productResponseDto.getDescription())))
                .andExpect(jsonPath("$.version", is(productResponseDto.getVersion())))
                .andExpect(jsonPath("$.category.id", is(productResponseDto.getCategory().getId()), Long.class))
                .andExpect(jsonPath("$.category.name", is(productResponseDto.getCategory().getName())))
                .andExpect(jsonPath("$.license", is(productResponseDto.getLicense().toString())))
                .andExpect(jsonPath("$.vendor.id", is(productResponseDto.getVendor().getId()), Long.class))
                .andExpect(jsonPath("$.vendor.name", is(productResponseDto.getVendor().getName())))
                .andExpect(jsonPath("$.vendor.description", is(productResponseDto.getVendor().getDescription())))
                .andExpect(jsonPath("$.vendor.country", is(productResponseDto.getVendor().getCountry().toString())))
                .andExpect(jsonPath("$.seller.id", is(productResponseDto.getSeller().getId()), Long.class))
                .andExpect(jsonPath("$.seller.email", is(productResponseDto.getSeller().getEmail())))
                .andExpect(jsonPath("$.seller.name", is(productResponseDto.getSeller().getName())))
                .andExpect(jsonPath("$.seller.phone", is(productResponseDto.getSeller().getPhone())))
                .andExpect(jsonPath("$.seller.description", is(productResponseDto.getSeller().getDescription())))
                .andExpect(jsonPath("$.seller.requisites.account", is(productResponseDto.getSeller().getRequisites().getAccount())))
                .andExpect(jsonPath("$.price", is(productResponseDto.getPrice()), Float.class))
                .andExpect(jsonPath("$.quantity", is(productResponseDto.getQuantity()), Integer.class))
                .andExpect(jsonPath("$.installation", is(productResponseDto.getInstallation()), Boolean.class))
                .andExpect(jsonPath("$.productStatus", is(productResponseDto.getProductStatus().toString())))
                .andExpect(jsonPath("$.productAvailability", is(productResponseDto.getProductAvailability()), Boolean.class));
    }

    @Test
    @DisplayName("Вызов метода updateProductTest: обновление продукта")
    void updateProductTest() throws Exception {
        productResponseDto.setProductStatus(ProductStatus.PUBLISHED);

        when(productService
                .updateProduct(anyLong(), anyLong(), any()))
                .thenReturn(productResponseDto);
        mockMvc.perform(patch("/seller/{sellerId}/product/{productId}", sellerId, productId)
                        .content(mapper.writeValueAsString(productDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(productResponseDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(productResponseDto.getName())))
                .andExpect(jsonPath("$.description", is(productResponseDto.getDescription())))
                .andExpect(jsonPath("$.version", is(productResponseDto.getVersion())))
                .andExpect(jsonPath("$.category.id", is(productResponseDto.getCategory().getId()), Long.class))
                .andExpect(jsonPath("$.category.name", is(productResponseDto.getCategory().getName())))
                .andExpect(jsonPath("$.license", is(productResponseDto.getLicense().toString())))
                .andExpect(jsonPath("$.vendor.id", is(productResponseDto.getVendor().getId()), Long.class))
                .andExpect(jsonPath("$.vendor.name", is(productResponseDto.getVendor().getName())))
                .andExpect(jsonPath("$.vendor.description", is(productResponseDto.getVendor().getDescription())))
                .andExpect(jsonPath("$.vendor.country", is(productResponseDto.getVendor().getCountry().toString())))
                .andExpect(jsonPath("$.seller.id", is(productResponseDto.getSeller().getId()), Long.class))
                .andExpect(jsonPath("$.seller.email", is(productResponseDto.getSeller().getEmail())))
                .andExpect(jsonPath("$.seller.name", is(productResponseDto.getSeller().getName())))
                .andExpect(jsonPath("$.seller.phone", is(productResponseDto.getSeller().getPhone())))
                .andExpect(jsonPath("$.seller.description", is(productResponseDto.getSeller().getDescription())))
                .andExpect(jsonPath("$.seller.requisites.account", is(productResponseDto.getSeller().getRequisites().getAccount())))
                .andExpect(jsonPath("$.price", is(productResponseDto.getPrice()), Float.class))
                .andExpect(jsonPath("$.quantity", is(productResponseDto.getQuantity()), Integer.class))
                .andExpect(jsonPath("$.installation", is(productResponseDto.getInstallation()), Boolean.class))
                .andExpect(jsonPath("$.productStatus", is(productResponseDto.getProductStatus().toString())))
                .andExpect(jsonPath("$.productAvailability", is(productResponseDto.getProductAvailability()), Boolean.class));

    }

    @Test
    @DisplayName("Вызов метода updateStatusProductOnSentTest: обновление статуса товара на 'SHIPPED'")
    void updateStatusProductOnSentTest() throws Exception {
        productResponseDto.setProductStatus(ProductStatus.SHIPPED);
        when(productService
                .updateStatusProductOnSent(anyLong(), anyLong()))
                .thenReturn(productResponseDto);
        mockMvc.perform(patch("/seller/1/product/1/send")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Share-Product-Id", productResponseDto.getId())
                        .param("productId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productStatus", is("SHIPPED")));
    }
}