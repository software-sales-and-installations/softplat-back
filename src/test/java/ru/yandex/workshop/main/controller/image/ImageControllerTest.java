//package ru.yandex.workshop.main.controller.image;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import lombok.SneakyThrows;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.Disabled;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.mock.web.MockMultipartFile;
//import org.springframework.test.annotation.DirtiesContext;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.MvcResult;
//import org.springframework.util.StringUtils;
//import ru.yandex.workshop.main.dto.image.ImageResponseDto;
//import ru.yandex.workshop.main.dto.product.ProductDto;
//import ru.yandex.workshop.main.dto.product.ProductResponseDto;
//import ru.yandex.workshop.main.dto.vendor.VendorResponseDto;
//import ru.yandex.workshop.main.model.product.License;
//import ru.yandex.workshop.main.dto.user.response.SellerResponseDto;
//import ru.yandex.workshop.main.dto.user.SellerDto;
//
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.IOException;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertNull;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@Disabled
//@SpringBootTest
//@AutoConfigureMockMvc
//@AutoConfigureTestDatabase
//@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
//class ImageControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    private static MockMultipartFile multipartFile;
//
//    private static SellerDto sellerDto;
//
//    private static ProductDto productDto;
//
//    @BeforeAll
//    static void init() {
//        long categoryId = 1, vendorId = 1, sellerId = 1;
//        sellerDto = SellerDto.builder()
//                .name("Joe")
//                .email("joedoe@email.com")
//                .phone("0123456789")
//                .build();
//
//        productDto = ProductDto.builder()
//                .name("Product name")
//                .description("Description product")
//                .version("2.0.0.1")
//                .category(categoryId)
//                .license(License.LICENSE)
//                .vendor(vendorId)
//                .seller(sellerId)
//                .price(1000.421F)
//                .quantity(5)
//                .installation(true)
//                .productAvailability(true)
//                .installationPrice(10.00F)
//                .build();
//
//        try {
//            String filePath = "src/test/resources/mockito-junit5-logo3.png";
//            File imageFile = new File(filePath);
//            FileInputStream fileInputStream = new FileInputStream(imageFile);
//
//            multipartFile = new MockMultipartFile(
//                    "image",
//                    imageFile.getName(),
//                    "image/png",
//                    fileInputStream
//            );
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Test
//    @SneakyThrows
//    void shouldAddImageToVendor() {
//        long vendorId = 1;
//        long imageId = 1;
//
//        MvcResult result = mockMvc.perform(multipart(
//                "http://localhost:8080/vendor/{vendorId}/image", vendorId)
//                        .file(multipartFile)
//                        .contentType(MediaType.MULTIPART_FORM_DATA))
//                .andExpect(status().isOk())
//                .andReturn();
//
//        VendorResponseDto vendorResponseDto = objectMapper.readValue(
//                result.getResponse().getContentAsString(),
//                VendorResponseDto.class);
//
//        ImageResponseDto imageResponseDto = vendorResponseDto.getImageResponseDto();
//        assertEquals(imageResponseDto.getId(), 1);
//        assertEquals(imageResponseDto.getName(), StringUtils.cleanPath(multipartFile.getOriginalFilename()));
//        assertEquals(imageResponseDto.getSize(), multipartFile.getSize());
//        assertEquals(imageResponseDto.getContentType(), multipartFile.getContentType());
//
//        String expectedUrl =  String.format("http://localhost:8080/image/%s", imageId);
//        assertEquals(imageResponseDto.getUrl(), expectedUrl);
//    }
//
//    @Test
//    @SneakyThrows
//    void shouldAddImageToSeller() {
//        long imageId = 1;
//
//        SellerResponseDto sellerResponseDto = createSeller(sellerDto);
//
//        assertNull(sellerResponseDto.getImageResponseDto());
//
//        String sellerEmail = sellerResponseDto.getEmail();
//
//        MvcResult result = mockMvc.perform(multipart(
//                        "http://localhost:8080/seller/account/image", sellerEmail)
//                        .file(multipartFile)
//                        .contentType(MediaType.MULTIPART_FORM_DATA))
//                .andExpect(status().isOk())
//                .andReturn();
//
//        sellerResponseDto = objectMapper.readValue(
//                result.getResponse().getContentAsString(),
//                SellerResponseDto.class);
//
//        ImageResponseDto imageResponseDto = sellerResponseDto.getImageResponseDto();
//        assertEquals(imageResponseDto.getId(), 1);
//        assertEquals(imageResponseDto.getName(), StringUtils.cleanPath(multipartFile.getOriginalFilename()));
//        assertEquals(imageResponseDto.getSize(), multipartFile.getSize());
//        assertEquals(imageResponseDto.getContentType(), multipartFile.getContentType());
//
//        String expectedUrl =  String.format("http://localhost:8080/image/%s", imageId);
//        assertEquals(imageResponseDto.getUrl(), expectedUrl);
//    }
//
//    @Test
//    @SneakyThrows
//    void shouldAddProductImage() {
//        long sellerId = 1, productId = 1, imageId = 1;
//
//        createSeller(sellerDto);
//        ProductResponseDto productResponseDto = create(productDto);
//
//        assertNull(productResponseDto.getImage());
//
//        MvcResult result = mockMvc.perform(multipart(
//                        "http://localhost:8080/product/{productId}/image", sellerId, productId)
//                        .file(multipartFile)
//                        .contentType(MediaType.MULTIPART_FORM_DATA))
//                .andExpect(status().isOk())
//                .andReturn();
//
//        productResponseDto = objectMapper.readValue(
//                result.getResponse().getContentAsString(),
//                ProductResponseDto.class);
//
//        ImageResponseDto imageResponseDto = productResponseDto.getImage();
//        assertEquals(imageResponseDto.getId(), 1);
//        assertEquals(imageResponseDto.getName(), StringUtils.cleanPath(multipartFile.getOriginalFilename()));
//        assertEquals(imageResponseDto.getSize(), multipartFile.getSize());
//        assertEquals(imageResponseDto.getContentType(), multipartFile.getContentType());
//
//        String expectedUrl =  String.format("http://localhost:8080/image/%s", imageId);
//        assertEquals(imageResponseDto.getUrl(), expectedUrl);
//    }
//
//    private SellerResponseDto createSeller(SellerDto sellerDto) throws Exception {
//        MvcResult result = mockMvc.perform(post("/seller/registration")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(sellerDto)))
//                .andExpect(status().isOk())
//                .andReturn();
//
//        return objectMapper.readValue(
//                result.getResponse().getContentAsString(),
//                SellerResponseDto.class);
//    }
//
//    private ProductResponseDto create(ProductDto productDto) throws Exception {
//        MvcResult result = mockMvc.perform(post("/seller/product")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(productDto)))
//                .andExpect(status().isOk())
//                .andReturn();
//
//        return objectMapper.readValue(
//                result.getResponse().getContentAsString(),
//                ProductResponseDto.class);
//    }
//}
