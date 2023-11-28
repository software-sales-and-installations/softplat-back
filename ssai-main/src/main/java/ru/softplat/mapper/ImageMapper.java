package ru.softplat.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

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
