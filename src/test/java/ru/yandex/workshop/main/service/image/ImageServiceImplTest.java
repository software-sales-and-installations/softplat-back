package ru.yandex.workshop.main.service.image;

import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import ru.yandex.workshop.main.dto.image.ImageDto;
import ru.yandex.workshop.main.dto.image.ImageMapper;
import ru.yandex.workshop.main.exception.EntityNotFoundException;
import ru.yandex.workshop.main.model.image.Image;
import ru.yandex.workshop.main.repository.image.ImageRepository;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ImageServiceImplTest {

    @InjectMocks
    private ImageServiceImpl imageService;
    @Mock
    private ImageRepository imageRepository;

    private MultipartFile multipartFile;
    private Image image;

    @BeforeEach
    void setUp() {
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

            image = Image.builder()
                    .name(StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename())))
                    .contentType(multipartFile.getContentType())
                    .bytes(multipartFile.getBytes())
                    .size(multipartFile.getSize())
                    .build();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    @SneakyThrows
    void addNewImage_whenValid_thenReturnImageDto() {
        when(imageRepository.save(any())).thenReturn(image);

        ImageDto expected = ImageMapper.INSTANCE.imageToImageDto(image);
        ImageDto actual = imageService.addNewImage(multipartFile);
        assertEquals(expected, actual);
    }

    @Test
    void getImageAsByteArray_whenImageIdNotValid_throwEntityNotFoundException() {
        long imageId = 1L;

        when(imageRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> imageService.getImageAsByteArray(imageId));
    }

    @Test
    void getImageAsByteArray_whenValid_thenReturnResponseWithByteArray() {
        when(imageRepository.findById(anyLong()))
                .thenReturn(Optional.of(image));

        ResponseEntity<byte[]> response = imageService.getImageAsByteArray(1L);
        String contentDisposition = "attachment; filename=\"" + image.getName() + "\"";
        assertEquals(contentDisposition, response.getHeaders().get(HttpHeaders.CONTENT_DISPOSITION).get(0));
        assertEquals(MediaType.IMAGE_PNG, response.getHeaders().getContentType());
        assertArrayEquals(image.getBytes(), response.getBody());
    }

    @Test
    void deleteImageById_whenValid_then() {
        long imageId = 1L;

        when(imageRepository.findById(anyLong()))
                .thenReturn(Optional.of(image));

        imageService.deleteImageById(imageId);
        verify(imageRepository).findById(imageId);
        verify(imageRepository).deleteById(imageId);
    }
}