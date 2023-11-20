package ru.yandex.workshop.main.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.yandex.workshop.main.dto.image.ImageDto;
import ru.yandex.workshop.main.dto.image.ImageResponseDto;
import ru.yandex.workshop.main.model.image.Image;

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

    ImageDto imageToImageDto(Image image);

    Image imageDtoToImage(ImageDto imageDto);
}
