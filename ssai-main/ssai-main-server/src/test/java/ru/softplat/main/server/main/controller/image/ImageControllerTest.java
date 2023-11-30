package ru.softplat.main.server.main.controller.image;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.util.StringUtils;
import ru.yandex.workshop.main.controller.AbstractControllerTest;
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
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ImageControllerTest extends AbstractControllerTest {

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
    void createVendorImage_shouldAddImageToVendor_whenMultipartFileIsValid() {
        long vendorId = vendorResponseDto.getId();
        long imageId = 1;

        assertNull(vendorResponseDto.getImage());

        MvcResult result = mockMvc.perform(multipart(
                        "http://localhost:8080/vendor/{vendorId}/image", vendorId)
                        .file(multipartFile)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isCreated())
                .andReturn();

        vendorResponseDto = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                VendorResponseDto.class);

        ImageResponseDto actualImageResponseDto = vendorResponseDto.getImage();
        assertEquals(actualImageResponseDto.getId(), 1);
        assertEquals(actualImageResponseDto.getName(), StringUtils.cleanPath(multipartFile.getOriginalFilename()));
        assertEquals(actualImageResponseDto.getSize(), multipartFile.getSize());
        assertEquals(actualImageResponseDto.getContentType(), multipartFile.getContentType());

        String expectedUrl = String.format("http://localhost:8080/image/%s", imageId);
        assertEquals(actualImageResponseDto.getUrl(), expectedUrl);
    }

    @Test
    @SneakyThrows
    @WithMockUser(username = "seller1@email.ru", authorities = {"seller:write"})
    void createSellerImage_shouldAddImageToSeller_whenMultipartFileIsValid() {
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

        ImageResponseDto actualImageResponseDto = sellerResponseDto.getImageResponseDto();
        assertEquals(actualImageResponseDto.getId(), 1);
        assertEquals(actualImageResponseDto.getName(), StringUtils.cleanPath(multipartFile.getOriginalFilename()));
        assertEquals(actualImageResponseDto.getSize(), multipartFile.getSize());
        assertEquals(actualImageResponseDto.getContentType(), multipartFile.getContentType());

        String expectedUrl = String.format("http://localhost:8080/image/%s", imageId);
        assertEquals(actualImageResponseDto.getUrl(), expectedUrl);
    }

    @Test
    @SneakyThrows
    @WithMockUser(username = "seller1@email.ru", authorities = {"seller:write"})
    void createProductImage_shouldAddProductImage_whenMultipartFileIsValid() {
        long productId = productResponseDto.getId();
        long imageId = 1;

        assertNull(productResponseDto.getImage());

        MvcResult result = mockMvc.perform(multipart(
                        "http://localhost:8080/product/{productId}/image/create", productId)
                        .file(multipartFile)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isCreated())
                .andReturn();

        productResponseDto = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                ProductResponseDto.class);

        ImageResponseDto actualImageResponseDto = productResponseDto.getImage();
        assertEquals(actualImageResponseDto.getId(), 1);
        assertEquals(actualImageResponseDto.getName(), StringUtils.cleanPath(multipartFile.getOriginalFilename()));
        assertEquals(actualImageResponseDto.getSize(), multipartFile.getSize());
        assertEquals(actualImageResponseDto.getContentType(), multipartFile.getContentType());

        String expectedUrl = String.format("http://localhost:8080/image/%s", imageId);
        assertEquals(actualImageResponseDto.getUrl(), expectedUrl);
    }
}