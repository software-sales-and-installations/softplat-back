package ru.yandex.workshop.main.service.image;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import ru.yandex.workshop.main.dto.image.ImageDto;
import ru.yandex.workshop.main.exception.EntityNotFoundException;
import ru.yandex.workshop.main.exception.ImageUploadingError;
import ru.yandex.workshop.main.mapper.ImageMapper;
import ru.yandex.workshop.main.message.ExceptionMessage;
import ru.yandex.workshop.main.model.image.Image;
import ru.yandex.workshop.main.repository.image.ImageRepository;

import java.io.IOException;
import java.util.Objects;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    private final ImageRepository imageRepository;
    private final ImageMapper imageMapper;

    @Override
    public ImageDto addNewImage(MultipartFile file) {
        try {
            Image image = Image.builder()
                    .name(StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename())))
                    .contentType(file.getContentType())
                    .bytes(file.getBytes())
                    .size(file.getSize())
                    .build();
            return imageMapper.imageToImageDto(imageRepository.save(image));
        } catch (IOException e) {
            log.error(ExceptionMessage.IMAGE_UPLOADING_ERROR.label);
            throw new ImageUploadingError(ExceptionMessage.IMAGE_UPLOADING_ERROR.label);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<byte[]> getImageAsByteArray(Long imageId) {
        Image image = getImageOrThrowException(imageId);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + image.getName() + "\"")
                .contentType(MediaType.valueOf(image.getContentType()))
                .body(image.getBytes());
    }

    @Override
    public void deleteImageById(Long imageId) {
        getImageOrThrowException(imageId);
        imageRepository.deleteById(imageId);
    }

    @Override
    @Transactional(readOnly = true)
    public Image getImage(Long imageId) {
        return imageRepository.findById(imageId).orElseThrow(
                () -> new EntityNotFoundException(ExceptionMessage.ENTITY_NOT_FOUND_EXCEPTION.name())
        );
    }

    private Image getImageOrThrowException(Long imageId) {
        return imageRepository.findById(imageId).orElseThrow(
                () -> new EntityNotFoundException(ExceptionMessage.ENTITY_NOT_FOUND_EXCEPTION.label)
        );
    }
}
