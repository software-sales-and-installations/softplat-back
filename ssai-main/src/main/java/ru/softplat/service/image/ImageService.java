package ru.softplat.service.image;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import ru.softplat.model.image.Image;

public interface ImageService {

    Image addNewImage(MultipartFile file);

    ResponseEntity<byte[]> getImageAsByteArray(Long imageId);

    void deleteImageById(Long imageId);

    Image getImageById(Long imageId);
}
