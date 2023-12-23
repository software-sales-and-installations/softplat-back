package ru.softplat.main.server.service.image;

import org.springframework.http.ResponseEntity;
import ru.softplat.main.server.model.image.Image;

public interface ImageService {

    Image addNewImage(Image image);

    ResponseEntity<byte[]> getImageAsByteArray(Long imageId);

    void deleteImageById(Long imageId);

    Image getImageById(Long imageId);
}
