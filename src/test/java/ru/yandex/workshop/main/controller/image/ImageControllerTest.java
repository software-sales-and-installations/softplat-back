package ru.yandex.workshop.main.controller.image;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.util.StringUtils;
import ru.yandex.workshop.main.controller.CrudOperations;
import ru.yandex.workshop.main.dto.image.ImageResponseDto;
import ru.yandex.workshop.main.dto.product.ProductResponseDto;
import ru.yandex.workshop.main.dto.vendor.VendorResponseDto;
import ru.yandex.workshop.main.dto.user.response.SellerResponseDto;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Sql("/data-test.sql")
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ImageControllerTest extends CrudOperations {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    private MockMultipartFile multipartFile;
    private VendorResponseDto vendorResponseDto;
    private SellerResponseDto sellerResponseDto;
    private ProductResponseDto productResponseDto;

    @BeforeEach
    void init() {
        sellerResponseDto = getSellerResponseDto(1L);
        productResponseDto = getProductResponseDto(1L);
        vendorResponseDto = getVendorResponseDto(1L);

        try {
            String filePath = "src/test/resources/mockito-junit5-logo3.png";
            File imageFile = new File(filePath);
            FileInputStream fileInputStream = new FileInputStream(imageFile);

            multipartFile = new MockMultipartFile(
                    "image",
                    imageFile.getName(),
                    "image/png",
                    fileInputStream
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    @SneakyThrows
    @WithMockUser(authorities = {"admin:write"})
    void shouldAddImageToVendor() {
        long vendorId = vendorResponseDto.getId();
        long imageId = 1;

        assertNull(vendorResponseDto.getImageResponseDto());

        MvcResult result = mockMvc.perform(multipart(
                        "http://localhost:8080/vendor/{vendorId}/image", vendorId)
                        .file(multipartFile)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isCreated())
                .andReturn();

        vendorResponseDto = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                VendorResponseDto.class);

        ImageResponseDto imageResponseDto = vendorResponseDto.getImageResponseDto();
        assertEquals(imageResponseDto.getId(), 1);
        assertEquals(imageResponseDto.getName(), StringUtils.cleanPath(multipartFile.getOriginalFilename()));
        assertEquals(imageResponseDto.getSize(), multipartFile.getSize());
        assertEquals(imageResponseDto.getContentType(), multipartFile.getContentType());

        String expectedUrl = String.format("http://localhost:8080/image/%s", imageId);
        assertEquals(imageResponseDto.getUrl(), expectedUrl);
    }

    @Test
    @SneakyThrows
    @WithMockUser(username = "seller1@email.ru", authorities = {"seller:write"})
    void shouldAddImageToSeller() {
        long imageId = 1;

        assertNull(sellerResponseDto.getImageResponseDto());

        MvcResult result = mockMvc.perform(multipart(
                        "http://localhost:8080/seller/account/image")
                        .file(multipartFile)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isCreated())
                .andReturn();

        sellerResponseDto = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                SellerResponseDto.class);

        ImageResponseDto imageResponseDto = sellerResponseDto.getImageResponseDto();
        assertEquals(imageResponseDto.getId(), 1);
        assertEquals(imageResponseDto.getName(), StringUtils.cleanPath(multipartFile.getOriginalFilename()));
        assertEquals(imageResponseDto.getSize(), multipartFile.getSize());
        assertEquals(imageResponseDto.getContentType(), multipartFile.getContentType());

        String expectedUrl = String.format("http://localhost:8080/image/%s", imageId);
        assertEquals(imageResponseDto.getUrl(), expectedUrl);
    }

    @Test
    @SneakyThrows
    @WithMockUser(username = "seller1@email.ru", authorities = {"seller:write"})
    void shouldAddProductImage() {
        long productId = productResponseDto.getId();
        long imageId = 1;

        assertNull(productResponseDto.getImage());

        MvcResult result = mockMvc.perform(multipart(
                        "http://localhost:8080/product/{productId}/image", productId)
                        .file(multipartFile)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isCreated())
                .andReturn();

        productResponseDto = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                ProductResponseDto.class);

        ImageResponseDto imageResponseDto = productResponseDto.getImage();
        assertEquals(imageResponseDto.getId(), 1);
        assertEquals(imageResponseDto.getName(), StringUtils.cleanPath(multipartFile.getOriginalFilename()));
        assertEquals(imageResponseDto.getSize(), multipartFile.getSize());
        assertEquals(imageResponseDto.getContentType(), multipartFile.getContentType());

        String expectedUrl = String.format("http://localhost:8080/image/%s", imageId);
        assertEquals(imageResponseDto.getUrl(), expectedUrl);
    }
}
