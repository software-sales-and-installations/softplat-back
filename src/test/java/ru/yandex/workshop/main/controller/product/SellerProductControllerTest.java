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
    static ProductDto productDto;
    static Vendor vendor;
    static Seller seller;
    static Image image;
    static Category category;
    static BankRequisites bankRequisites;
    static LocalDateTime time;
    static String foramttedString;

    static List<ProductDto> productDtoList;

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
                true);

        productDto = new ProductDto(
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
                true);

        productDtoList = List.of(ProductMapper.INSTANCE.productToProductDto(product), productDto);
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
                .andExpect(jsonPath("$.id", is(productDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(productDto.getName().toString())))
                .andExpect(jsonPath("$.description", is(productDto.getDescription().toString())))
                .andExpect(jsonPath("$.version", is(productDto.getVersion().toString())))
                .andExpect(jsonPath("$.image.id", is(productDto.getImage().getId()), Long.class))
                .andExpect(jsonPath("$.image.name", is(productDto.getImage().getName().toString())))
                .andExpect(jsonPath("$.image.size", is(productDto.getImage().getSize()), Float.class))
                .andExpect(jsonPath("$.image.contentType", is(productDto.getImage().getContentType().toString())))
                //TODO - добавить проверку bytes
                .andExpect(jsonPath("$.category.id", is(productDto.getCategory().getId()), Long.class))
                .andExpect(jsonPath("$.category.name", is(productDto.getCategory().getName().toString())))
                .andExpect(jsonPath("$.license", is(productDto.getLicense().toString())))
                .andExpect(jsonPath("$.vendor.id", is(productDto.getVendor().getId()), Long.class))
                .andExpect(jsonPath("$.vendor.name", is(productDto.getVendor().getName().toString())))
                .andExpect(jsonPath("$.vendor.description", is(productDto.getVendor().getDescription().toString())))
                .andExpect(jsonPath("$.vendor.imageId", is(productDto.getVendor().getImageId()), Long.class))
                .andExpect(jsonPath("$.vendor.country", is(productDto.getVendor().getCountry().toString())))
                .andExpect(jsonPath("$.seller.id", is(productDto.getSeller().getId()), Long.class))
                .andExpect(jsonPath("$.seller.email", is(productDto.getSeller().getEmail().toString())))
                .andExpect(jsonPath("$.seller.name", is(productDto.getSeller().getName().toString())))
                .andExpect(jsonPath("$.seller.phone", is(productDto.getSeller().getPhone().toString())))
                .andExpect(jsonPath("$.seller.description", is(productDto.getSeller().getDescription().toString())))
                .andExpect(jsonPath("$.seller.requisites.id", is(productDto.getSeller().getRequisites().getId()), Long.class))
                .andExpect(jsonPath("$.seller.requisites.account", is(productDto.getSeller().getRequisites().getAccount().toString())))
                .andExpect(jsonPath("$.seller.image.name", is(productDto.getSeller().getImage().getName().toString())))
                .andExpect(jsonPath("$.seller.image.size", is(productDto.getSeller().getImage().getSize()), Float.class))
                .andExpect(jsonPath("$.seller.image.contentType", is(productDto.getSeller().getImage().getContentType().toString())))
                .andExpect(jsonPath("$.price", is(productDto.getPrice()), Float.class))
                .andExpect(jsonPath("$.quantity", is(productDto.getQuantity()), Integer.class))
                .andExpect(jsonPath("$.installation", is(productDto.getInstallation()), Boolean.class))
                .andExpect(jsonPath("$.productStatus", is(productDto.getProductStatus().toString())))
                .andExpect(jsonPath("$.productAvailability", is(productDto.getProductAvailability()), Boolean.class));
    }

    @Test
    @DisplayName("Вызов метода createProductTest: создание продукта")
    void createProductTest() throws Exception {
        when(productService
                .createProduct(any()))
                .thenReturn(productDto);
        mockMvc.perform(post("/seller/product")
                        .content(mapper.writeValueAsString(productDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Share-Product-Id", productDto.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(productDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(productDto.getName().toString())))
                .andExpect(jsonPath("$.description", is(productDto.getDescription().toString())))
                .andExpect(jsonPath("$.version", is(productDto.getVersion().toString())))
                .andExpect(jsonPath("$.image.id", is(productDto.getImage().getId()), Long.class))
                .andExpect(jsonPath("$.image.name", is(productDto.getImage().getName().toString())))
                .andExpect(jsonPath("$.image.size", is(productDto.getImage().getSize()), Float.class))
                .andExpect(jsonPath("$.image.contentType", is(productDto.getImage().getContentType().toString())))
                //TODO - добавить проверку bytes
                .andExpect(jsonPath("$.category.id", is(productDto.getCategory().getId()), Long.class))
                .andExpect(jsonPath("$.category.name", is(productDto.getCategory().getName().toString())))
                .andExpect(jsonPath("$.license", is(productDto.getLicense().toString())))
                .andExpect(jsonPath("$.vendor.id", is(productDto.getVendor().getId()), Long.class))
                .andExpect(jsonPath("$.vendor.name", is(productDto.getVendor().getName().toString())))
                .andExpect(jsonPath("$.vendor.description", is(productDto.getVendor().getDescription().toString())))
                .andExpect(jsonPath("$.vendor.imageId", is(productDto.getVendor().getImageId()), Long.class))
                .andExpect(jsonPath("$.vendor.country", is(productDto.getVendor().getCountry().toString())))
                .andExpect(jsonPath("$.seller.id", is(productDto.getSeller().getId()), Long.class))
                .andExpect(jsonPath("$.seller.email", is(productDto.getSeller().getEmail().toString())))
                .andExpect(jsonPath("$.seller.name", is(productDto.getSeller().getName().toString())))
                .andExpect(jsonPath("$.seller.phone", is(productDto.getSeller().getPhone().toString())))
                .andExpect(jsonPath("$.seller.description", is(productDto.getSeller().getDescription().toString())))
                .andExpect(jsonPath("$.seller.requisites.id", is(productDto.getSeller().getRequisites().getId()), Long.class))
                .andExpect(jsonPath("$.seller.requisites.account", is(productDto.getSeller().getRequisites().getAccount().toString())))
                .andExpect(jsonPath("$.seller.image.name", is(productDto.getSeller().getImage().getName().toString())))
                .andExpect(jsonPath("$.seller.image.size", is(productDto.getSeller().getImage().getSize()), Float.class))
                .andExpect(jsonPath("$.seller.image.contentType", is(productDto.getSeller().getImage().getContentType().toString())))
                .andExpect(jsonPath("$.price", is(productDto.getPrice()), Float.class))
                .andExpect(jsonPath("$.quantity", is(productDto.getQuantity()), Integer.class))
                .andExpect(jsonPath("$.installation", is(productDto.getInstallation()), Boolean.class))
                .andExpect(jsonPath("$.productStatus", is(productDto.getProductStatus().toString())))
                .andExpect(jsonPath("$.productAvailability", is(productDto.getProductAvailability()), Boolean.class));
    }

    @Test
    @DisplayName("Вызов метода updateProductTest: обновление продукта")
    void updateProductTest() throws Exception {
        ProductForUpdate productForUpdate = ProductMapper.INSTANCE.productToProductForUpdate(product);

        when(productService
                .updateProduct(anyLong(), any()))
                .thenReturn(productDto);
        mockMvc.perform(patch("/seller/1/product")
                        .content(mapper.writeValueAsString(productForUpdate))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Share-Product-Id", productForUpdate.getId())
                        .param("sellerId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(productForUpdate.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(productForUpdate.getName().toString())))
                .andExpect(jsonPath("$.description", is(productForUpdate.getDescription().toString())))
                .andExpect(jsonPath("$.version", is(productForUpdate.getVersion().toString())))
                .andExpect(jsonPath("$.image.id", is(productForUpdate.getImage().getId()), Long.class))
                .andExpect(jsonPath("$.image.name", is(productForUpdate.getImage().getName().toString())))
                .andExpect(jsonPath("$.image.size", is(productForUpdate.getImage().getSize()), Float.class))
                .andExpect(jsonPath("$.image.contentType", is(productForUpdate.getImage().getContentType().toString())))
                //TODO - добавить проверку bytes
                .andExpect(jsonPath("$.category.id", is(productForUpdate.getCategory().getId()), Long.class))
                .andExpect(jsonPath("$.category.name", is(productForUpdate.getCategory().getName().toString())))
                .andExpect(jsonPath("$.license", is(productForUpdate.getLicense().toString())))
                .andExpect(jsonPath("$.vendor.id", is(productForUpdate.getVendor().getId()), Long.class))
                .andExpect(jsonPath("$.vendor.name", is(productForUpdate.getVendor().getName().toString())))
                .andExpect(jsonPath("$.vendor.description", is(productForUpdate.getVendor().getDescription().toString())))
                .andExpect(jsonPath("$.vendor.imageId", is(productForUpdate.getVendor().getImageId()), Long.class))
                .andExpect(jsonPath("$.vendor.country", is(productForUpdate.getVendor().getCountry().toString())))
                .andExpect(jsonPath("$.seller.id", is(productForUpdate.getSeller().getId()), Long.class))
                .andExpect(jsonPath("$.seller.email", is(productForUpdate.getSeller().getEmail().toString())))
                .andExpect(jsonPath("$.seller.name", is(productForUpdate.getSeller().getName().toString())))
                .andExpect(jsonPath("$.seller.phone", is(productForUpdate.getSeller().getPhone().toString())))
                .andExpect(jsonPath("$.seller.description", is(productForUpdate.getSeller().getDescription().toString())))
                .andExpect(jsonPath("$.seller.requisites.id", is(productForUpdate.getSeller().getRequisites().getId()), Long.class))
                .andExpect(jsonPath("$.seller.requisites.account", is(productForUpdate.getSeller().getRequisites().getAccount().toString())))
                .andExpect(jsonPath("$.seller.image.name", is(productForUpdate.getSeller().getImage().getName().toString())))
                .andExpect(jsonPath("$.seller.image.size", is(productForUpdate.getSeller().getImage().getSize()), Float.class))
                .andExpect(jsonPath("$.seller.image.contentType", is(productForUpdate.getSeller().getImage().getContentType().toString())))
                .andExpect(jsonPath("$.price", is(productForUpdate.getPrice()), Float.class))
                .andExpect(jsonPath("$.quantity", is(productForUpdate.getQuantity()), Integer.class))
                .andExpect(jsonPath("$.installation", is(productForUpdate.getInstallation()), Boolean.class))
                .andExpect(jsonPath("$.productStatus", is(productForUpdate.getProductStatus().toString())))
                .andExpect(jsonPath("$.productAvailability", is(productForUpdate.getProductAvailability()), Boolean.class));
    }

    @Test
    @DisplayName("Вызов метода updateStatusProductOnSentTest: обновление статуса товара на 'SHIPPED'")
    void updateStatusProductOnSentTest() throws Exception {
        product.setProductStatus(ProductStatus.SHIPPED);
        ProductDto productDtoUpdate = ProductMapper.INSTANCE.productToProductDto(product);
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