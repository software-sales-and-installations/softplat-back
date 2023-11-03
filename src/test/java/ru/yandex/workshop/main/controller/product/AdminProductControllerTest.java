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
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.yandex.workshop.main.model.product.ProductStatus.DRAFT;

@WebMvcTest(controllers = AdminProductController.class)
class AdminProductControllerTest {

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
    @DisplayName("Вызов метода getAllProductsSellerTest: получение всех продуктов")
    void getAllProductsSellerTest() throws Exception {
        when(productService
                .getAllProductsSeller(anyInt(), anyInt()))
                .thenReturn(productDtoList);
        mockMvc.perform(get("/admin/products")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Share-Product-Id", productDto.getId())
                        .param("from", "0")
                        .param("size", "20"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(productDtoList)));
    }

    @Test
    @DisplayName("Вызов метода getProductsSellerTest: получение всех продуктов конкретного продавца")
    void getProductsSellerTest() throws Exception {
        when(productService
                .getProductsSeller(anyLong(), anyInt(), anyInt()))
                .thenReturn(productDtoList);
        mockMvc.perform(get("/admin/1/products")
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
    @DisplayName("Вызов метода getProductsSellerTest: получение продуктa по id")
    void getProductByIdAdminTest() throws Exception {
        when(productService
                .getProductByIdAdmin(anyLong()))
                .thenReturn(productDto);

        mockMvc.perform(get("/admin/product/1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Share-Product-Id", productDto.getId()))
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
                .andExpect(jsonPath("$.productAvailability", is(productDto.getProductAvailability()), Boolean.class));
    }

    @Test
    @DisplayName("Вызов метода updateStatusProductOnPublishedTest: обновление статуса товара на 'PUBLISHED'")
    void updateStatusProductOnPublishedTest() throws Exception {
        product.setProductStatus(ProductStatus.PUBLISHED);
        ProductDto productDtoUpdate = ProductMapper.INSTANCE.productToProductDto(product);
        productDtoUpdate.setProductStatus(ProductStatus.PUBLISHED);
        when(productService
                .updateStatusProductOnPublished(anyLong()))
                .thenReturn(productDtoUpdate);
        mockMvc.perform(patch("/admin/product/1/published")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Share-Product-Id", productDtoUpdate.getId())
                        .param("productId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productStatus", is("PUBLISHED")));
    }

    @Test
    @DisplayName("Вызов метода updateStatusProductOnRejectedTest: обновление статуса товара на 'REJECTED'")
    void updateStatusProductOnRejectedTest() throws Exception {
        product.setProductStatus(ProductStatus.REJECTED);
        ProductDto productDtoUpdate = ProductMapper.INSTANCE.productToProductDto(product);
        productDtoUpdate.setProductStatus(ProductStatus.REJECTED);
        when(productService
                .updateStatusProductOnRejected(anyLong()))
                .thenReturn(productDtoUpdate);
        mockMvc.perform(patch("/admin/product/1/rejected")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Share-Product-Id", productDtoUpdate.getId())
                        .param("productId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productStatus", is("REJECTED")));
    }

    @Test
    @DisplayName("Вызов метода deleteProductAdminTest: удаление продукта")
    void deleteProductAdminTest() throws Exception {
        when(productService
                .updateStatusProductOnRejected(anyLong()))
                .thenReturn(productDto);
        mockMvc.perform(delete("/admin/product/1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Share-Product-Id", productDto.getId())
                        .param("productId", "1"))
                .andExpect(status().isOk());
    }
}
