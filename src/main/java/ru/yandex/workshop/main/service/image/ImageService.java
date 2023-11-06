package ru.yandex.workshop.main.service.image;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import ru.yandex.workshop.main.dto.image.ImageDto;

public interface ImageService {

    ImageDto addNewImage(MultipartFile file);

    ResponseEntity<byte[]> getImageAsByteArray(Long imageId);

    void deleteImageById(Long imageId);
}
