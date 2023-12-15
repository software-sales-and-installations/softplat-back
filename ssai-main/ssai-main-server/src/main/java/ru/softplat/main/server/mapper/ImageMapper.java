package ru.softplat.main.server.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.softplat.main.dto.image.ImageCreateDto;
import ru.softplat.main.dto.image.ImageResponseDto;
import ru.softplat.main.server.model.image.Image;

@Mapper
@Component
public interface ImageMapper {

    Image toImage(ImageCreateDto imageCreateDto);

    @Mapping(target = "url", expression = "java(mapImageUrl(image))")
    ImageResponseDto imageToImageResponseDto(Image image);

    default String mapImageUrl(Image image) {
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/image/")
                .path(String.valueOf(image.getId()))
                .toUriString();
    }
}
