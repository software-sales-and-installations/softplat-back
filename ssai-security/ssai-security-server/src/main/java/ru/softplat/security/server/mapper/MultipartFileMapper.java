package ru.softplat.security.server.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import ru.softplat.main.dto.image.ImageCreateDto;
import ru.softplat.security.server.exception.ImageServerUploadException;
import ru.softplat.security.server.message.ExceptionMessage;

import java.io.IOException;
import java.util.Objects;

@Mapper
@Component
public interface MultipartFileMapper {

    @Mapping(target = "name", expression = "java(getName(file))")
    @Mapping(target = "bytes", expression = "java(getBytes(file))")
    ImageCreateDto toImageDto(MultipartFile file);

    default String getName(MultipartFile file) {
        return StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
    }

    default byte[] getBytes(MultipartFile file) {
        try {
            return file.getBytes();
        } catch (IOException e) {
            throw new ImageServerUploadException(ExceptionMessage.IMAGE_SERVER_UPLOAD_EXCEPTION.label);
        }
    }
}
