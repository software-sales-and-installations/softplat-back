package ru.softplat.security.server.web.validation;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;
import ru.softplat.security.server.exception.ImageFormatException;
import ru.softplat.security.server.exception.ImagePayloadTooLargeException;
import ru.softplat.security.server.message.ExceptionMessage;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class MultipartFileFormatValidator implements ConstraintValidator<MultipartFileFormat, MultipartFile> {
    @Value("${multipart.image.max-file-size}")
    private String maxFileSizeConfig;
    private static final long MEGABYTE = 1024 * 1024;
    private long maxFileSize;

    @Override
    public void initialize(MultipartFileFormat constraintAnnotation) {
        maxFileSize = parseMaxFileSize(maxFileSizeConfig);
    }

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

        if (fileSize > maxFileSize) {
            throw new ImagePayloadTooLargeException(ExceptionMessage.IMAGE_SIZE_EXCEED_EXCEPTION.label);
        }

        return true;
    }


    private long parseMaxFileSize(String maxFileSizeConfig) {
        return Long.parseLong(maxFileSizeConfig) * MEGABYTE;
    }
}
