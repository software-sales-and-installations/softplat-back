package ru.softplat.main.server.service.image;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.softplat.main.server.exception.EntityNotFoundException;
import ru.softplat.main.server.message.ExceptionMessage;
import ru.softplat.main.server.model.image.Image;
import ru.softplat.main.server.repository.image.ImageRepository;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    private final ImageRepository imageRepository;

    @Override
    public Image addNewImage(Image image) {
        return imageRepository.save(image);
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<byte[]> getImageAsByteArray(Long imageId) {
        Image image = getImageById(imageId);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + image.getName() + "\"")
                .contentType(MediaType.valueOf(image.getContentType()))
                .body(image.getBytes());
    }

    @Override
    public void deleteImageById(Long imageId) {
        getImageById(imageId);
        imageRepository.deleteById(imageId);
    }

    @Override
    @Transactional(readOnly = true)
    public Image getImageById(Long imageId) {
        return imageRepository.findById(imageId).orElseThrow(
                () -> new EntityNotFoundException(
                        ExceptionMessage.ENTITY_NOT_FOUND_EXCEPTION.getMessage(imageId, Image.class)
                ));
    }
}
