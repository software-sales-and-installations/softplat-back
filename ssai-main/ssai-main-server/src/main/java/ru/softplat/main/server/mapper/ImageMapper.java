package ru.softplat.main.server.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.softplat.main.server.model.image.Image;
import ru.softplat.dto.image.ImageResponseDto;

@Mapper
@Component
public interface ImageMapper {

    @Mapping(target = "url", expression = "java(mapImageUrl(image))")
    ImageResponseDto imageToImageResponseDto(Image image);

    default String mapImageUrl(Image image) {
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/image/")
                .path(String.valueOf(image.getId()))
                .toUriString();
    }
}