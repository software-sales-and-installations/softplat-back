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
import ru.yandex.workshop.main.dto.product.ProductForUpdate;
import ru.yandex.workshop.main.dto.product.ProductMapper;
import ru.yandex.workshop.main.dto.product.ProductResponseDto;
import ru.yandex.workshop.main.model.image.Image;
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
import static ru.yandex.workshop.main.model.product.ProductStatus.DRAFT;

@WebMvcTest(controllers = SellerProductController.class)
class SellerProductControllerTest {

    @MockBean
    private ProductService productService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper = new ObjectMapper();

    static Product product;
    static ProductResponseDto productDto;
    static ProductForUpdate productForUpdate;
    static Vendor vendor;
    static Seller seller;
    static Image image;
    static Category category;
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

        image = new Image(
                1L,
                "name",
                123.12F,
                "contentType",
                new byte[]{0x01, 0x02, 0x03});

        vendor = new Vendor(
                1L,
                "name1",
                "Name One",
                1L,
                Country.RUSSIA);

        seller = new Seller(
                1L,
                "NameTwo@gmail.com",
                "Name",
                " +79111111111",
                "Description seller",
                time,
                bankRequisites,
                image);

        category = new Category(
                1L,
                "Category");

        product = new Product(
                1L,
                "Name product",
                "Description product",
                "2.0.0.1",
                time,
                image,
                category,
                License.LICENSE,
                vendor,
                seller,
                1000.421F,
                5,
                true,
                DRAFT,
                true,
                10.00F);


        productForUpdate = new ProductForUpdate(
                "Name product 2",
                "Description product 2",
                "2.0.0.5",
                time,
                image,
                category,
                License.LICENSE,
                vendor,
                seller,
                10.421F,
                0,
                false,
                DRAFT,
                false,
                null);

        productDto = new ProductResponseDto(
                1L,
                "Name product",
                "Description product",
                "2.0.0.1",
                time,
                image,
                category,
                License.LICENSE,
                vendor,
                seller,
                1000.421F,
                5,
                true,
                DRAFT,
                true,
                10.00F);

        productDtoList = List.of(ProductMapper.INSTANCE.productToProductResponseDto(product), productDto);
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
                        .header("X-Share-Product-Id", productDto.getId())
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
                .thenReturn(productDto);

        mockMvc.perform(get("/seller/1/product/1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Share-Product-Id", productDto.getId())
                        .param("sellerId", "1")
                        .param("productId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(productDto.getName())))
                .andExpect(jsonPath("$.description", is(productDto.getDescription())))
                .andExpect(jsonPath("$.version", is(productDto.getVersion())))
                .andExpect(jsonPath("$.image.id", is(productDto.getImage().getId()), Long.class))
                .andExpect(jsonPath("$.image.name", is(productDto.getImage().getName())))
                .andExpect(jsonPath("$.image.size", is(productDto.getImage().getSize()), Float.class))
                .andExpect(jsonPath("$.image.contentType", is(productDto.getImage().getContentType())))
                //TODO - добавить проверку bytes
                .andExpect(jsonPath("$.category.id", is(productDto.getCategory().getId()), Long.class))
                .andExpect(jsonPath("$.category.name", is(productDto.getCategory().getName())))
                .andExpect(jsonPath("$.license", is(productDto.getLicense().toString())))
                .andExpect(jsonPath("$.vendor.id", is(productDto.getVendor().getId()), Long.class))
                .andExpect(jsonPath("$.vendor.name", is(productDto.getVendor().getName())))
                .andExpect(jsonPath("$.vendor.description", is(productDto.getVendor().getDescription())))
                .andExpect(jsonPath("$.vendor.imageId", is(productDto.getVendor().getImageId()), Long.class))
                .andExpect(jsonPath("$.vendor.country", is(productDto.getVendor().getCountry().toString())))
                .andExpect(jsonPath("$.seller.id", is(productDto.getSeller().getId()), Long.class))
                .andExpect(jsonPath("$.seller.email", is(productDto.getSeller().getEmail())))
                .andExpect(jsonPath("$.seller.name", is(productDto.getSeller().getName())))
                .andExpect(jsonPath("$.seller.phone", is(productDto.getSeller().getPhone())))
                .andExpect(jsonPath("$.seller.description", is(productDto.getSeller().getDescription())))
                .andExpect(jsonPath("$.seller.requisites.id", is(productDto.getSeller().getRequisites().getId()), Long.class))
                .andExpect(jsonPath("$.seller.requisites.account", is(productDto.getSeller().getRequisites().getAccount())))
                .andExpect(jsonPath("$.seller.image.name", is(productDto.getSeller().getImage().getName())))
                .andExpect(jsonPath("$.seller.image.size", is(productDto.getSeller().getImage().getSize()), Float.class))
                .andExpect(jsonPath("$.seller.image.contentType", is(productDto.getSeller().getImage().getContentType())))
                .andExpect(jsonPath("$.price", is(productDto.getPrice()), Float.class))
                .andExpect(jsonPath("$.quantity", is(productDto.getQuantity()), Integer.class))
                .andExpect(jsonPath("$.installation", is(productDto.getInstallation()), Boolean.class))
                .andExpect(jsonPath("$.productStatus", is(productDto.getProductStatus().toString())))
                .andExpect(jsonPath("$.productAvailability", is(productDto.getProductAvailability()), Boolean.class))
                .andExpect(jsonPath("$.installationPrice", is(productDto.getInstallationPrice()), Float.class));
    }

    @Test
    @DisplayName("Вызов метода createProductTest: создание продукта")
    void createProductTest() throws Exception {
        when(productService
                .createProduct(any()))
                .thenReturn(productDto);
        mockMvc.perform(post("/seller/product")
                        .content(mapper.writeValueAsString(product))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Share-Product-Id", product.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(productDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(productDto.getName())))
                .andExpect(jsonPath("$.description", is(productDto.getDescription())))
                .andExpect(jsonPath("$.version", is(productDto.getVersion())))
                .andExpect(jsonPath("$.image.id", is(productDto.getImage().getId()), Long.class))
                .andExpect(jsonPath("$.image.name", is(productDto.getImage().getName())))
                .andExpect(jsonPath("$.image.size", is(productDto.getImage().getSize()), Float.class))
                .andExpect(jsonPath("$.image.contentType", is(productDto.getImage().getContentType())))
                //TODO - добавить проверку bytes
                .andExpect(jsonPath("$.category.id", is(productDto.getCategory().getId()), Long.class))
                .andExpect(jsonPath("$.category.name", is(productDto.getCategory().getName())))
                .andExpect(jsonPath("$.license", is(productDto.getLicense().toString())))
                .andExpect(jsonPath("$.vendor.id", is(productDto.getVendor().getId()), Long.class))
                .andExpect(jsonPath("$.vendor.name", is(productDto.getVendor().getName())))
                .andExpect(jsonPath("$.vendor.description", is(productDto.getVendor().getDescription())))
                .andExpect(jsonPath("$.vendor.imageId", is(productDto.getVendor().getImageId()), Long.class))
                .andExpect(jsonPath("$.vendor.country", is(productDto.getVendor().getCountry().toString())))
                .andExpect(jsonPath("$.seller.id", is(productDto.getSeller().getId()), Long.class))
                .andExpect(jsonPath("$.seller.email", is(productDto.getSeller().getEmail())))
                .andExpect(jsonPath("$.seller.name", is(productDto.getSeller().getName())))
                .andExpect(jsonPath("$.seller.phone", is(productDto.getSeller().getPhone())))
                .andExpect(jsonPath("$.seller.description", is(productDto.getSeller().getDescription())))
                .andExpect(jsonPath("$.seller.requisites.id", is(productDto.getSeller().getRequisites().getId()), Long.class))
                .andExpect(jsonPath("$.seller.requisites.account", is(productDto.getSeller().getRequisites().getAccount())))
                .andExpect(jsonPath("$.seller.image.name", is(productDto.getSeller().getImage().getName())))
                .andExpect(jsonPath("$.seller.image.size", is(productDto.getSeller().getImage().getSize()), Float.class))
                .andExpect(jsonPath("$.seller.image.contentType", is(productDto.getSeller().getImage().getContentType())))
                .andExpect(jsonPath("$.price", is(productDto.getPrice()), Float.class))
                .andExpect(jsonPath("$.quantity", is(productDto.getQuantity()), Integer.class))
                .andExpect(jsonPath("$.installation", is(productDto.getInstallation()), Boolean.class))
                .andExpect(jsonPath("$.productStatus", is(productDto.getProductStatus().toString())))
                .andExpect(jsonPath("$.productAvailability", is(productDto.getProductAvailability()), Boolean.class))
                .andExpect(jsonPath("$.installationPrice", is(productDto.getInstallationPrice()), Float.class));
    }

    @Test
    @DisplayName("Вызов метода updateProductTest: обновление продукта")
    void updateProductTest() throws Exception {
        ProductDto productDtoSave = ProductMapper.INSTANCE.productToProductDto(product);
        when(productService
                .createProduct(productDtoSave))
                .thenReturn(productDto);
        when(productService
                .updateProduct(anyLong(), anyLong(), any()))
                .thenReturn(productDto);
        mockMvc.perform(patch("/seller/1/product/1")
                        .content(mapper.writeValueAsString(product))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Share-Product-Id", product.getId())
                        .param("sellerId", "1")
                        .param("productForUpdate", "productForUpdate")
                        .param("productId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(productDto.getName())))
                .andExpect(jsonPath("$.description", is(productDto.getDescription())))
                .andExpect(jsonPath("$.version", is(productDto.getVersion())))
                .andExpect(jsonPath("$.image.id", is(productDto.getImage().getId()), Long.class))
                .andExpect(jsonPath("$.image.name", is(productDto.getImage().getName())))
                .andExpect(jsonPath("$.image.size", is(productDto.getImage().getSize()), Float.class))
                .andExpect(jsonPath("$.image.contentType", is(productDto.getImage().getContentType())))
                //TODO - добавить проверку bytes
                .andExpect(jsonPath("$.category.id", is(productDto.getCategory().getId()), Long.class))
                .andExpect(jsonPath("$.category.name", is(productDto.getCategory().getName())))
                .andExpect(jsonPath("$.license", is(productDto.getLicense().toString())))
                .andExpect(jsonPath("$.vendor.id", is(productDto.getVendor().getId()), Long.class))
                .andExpect(jsonPath("$.vendor.name", is(productDto.getVendor().getName())))
                .andExpect(jsonPath("$.vendor.description", is(productDto.getVendor().getDescription())))
                .andExpect(jsonPath("$.vendor.imageId", is(productDto.getVendor().getImageId()), Long.class))
                .andExpect(jsonPath("$.vendor.country", is(productDto.getVendor().getCountry().toString())))
                .andExpect(jsonPath("$.seller.id", is(productDto.getSeller().getId()), Long.class))
                .andExpect(jsonPath("$.seller.email", is(productDto.getSeller().getEmail())))
                .andExpect(jsonPath("$.seller.name", is(productDto.getSeller().getName())))
                .andExpect(jsonPath("$.seller.phone", is(productDto.getSeller().getPhone())))
                .andExpect(jsonPath("$.seller.description", is(productDto.getSeller().getDescription())))
                .andExpect(jsonPath("$.seller.requisites.id", is(productDto.getSeller().getRequisites().getId()), Long.class))
                .andExpect(jsonPath("$.seller.requisites.account", is(productDto.getSeller().getRequisites().getAccount())))
                .andExpect(jsonPath("$.seller.image.name", is(productDto.getSeller().getImage().getName())))
                .andExpect(jsonPath("$.seller.image.size", is(productDto.getSeller().getImage().getSize()), Float.class))
                .andExpect(jsonPath("$.seller.image.contentType", is(productDto.getSeller().getImage().getContentType())))
                .andExpect(jsonPath("$.price", is(productDto.getPrice()), Float.class))
                .andExpect(jsonPath("$.quantity", is(productDto.getQuantity()), Integer.class))
                .andExpect(jsonPath("$.installation", is(productDto.getInstallation()), Boolean.class))
                .andExpect(jsonPath("$.productStatus", is(productDto.getProductStatus().toString())))
                .andExpect(jsonPath("$.productAvailability", is(productDto.getProductAvailability()), Boolean.class));
    }

    @Test
    @DisplayName("Вызов метода updateStatusProductOnSentTest: обновление статуса товара на 'SHIPPED'")
    void updateStatusProductOnSentTest() throws Exception {
        product.setProductStatus(ProductStatus.SHIPPED);
        ProductResponseDto productDtoUpdate = ProductMapper.INSTANCE.productToProductResponseDto(product);
        productDtoUpdate.setProductStatus(ProductStatus.SHIPPED);
        when(productService
                .updateStatusProductOnSent(anyLong(), anyLong()))
                .thenReturn(productDtoUpdate);
        mockMvc.perform(patch("/seller/1/product/1/send")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Share-Product-Id", productDtoUpdate.getId())
                        .param("productId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productStatus", is("SHIPPED")));
    }
}