package ru.yandex.workshop.main.web.validation;

import org.springframework.web.multipart.MultipartFile;
import ru.yandex.workshop.main.exception.ImageFormatException;
import ru.yandex.workshop.main.exception.ImagePayloadTooLargeException;
import ru.yandex.workshop.main.message.ExceptionMessage;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class MultipartFileFormatValidator implements ConstraintValidator<MultipartFileFormat, MultipartFile> {
    public static final long MAX_SIZE_IMAGE = 5 * 1024 * 1024;

    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {
        if (file.isEmpty()) {
            throw new ImageFormatException(ExceptionMessage.NO_IMAGE_CONTENT_EXCEPTION.label);
        }

        String contentType = file.getContentType();
        if (contentType == null || (!contentType.equals("image/jpeg") && !contentType.equals("image/png"))) {
            throw new ImageFormatException(ExceptionMessage.IMAGE_FORMAT_EXCEPTION.label);
        }

        long fileSize = file.getSize();

        if (fileSize > MAX_SIZE_IMAGE) {
            throw new ImagePayloadTooLargeException("Размер изображения должен быть не больше 5 мегабайт.");
        }

        return true;
    }
}
