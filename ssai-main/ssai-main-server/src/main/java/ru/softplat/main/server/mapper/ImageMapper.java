package ru.softplat.main.server.mapper;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;
import ru.softplat.main.dto.image.ImageCreateDto;
import ru.softplat.main.dto.image.ImageResponseDto;
import ru.softplat.main.server.model.image.Image;

@Mapper
@Component
public interface ImageMapper {

    Image toImage(ImageCreateDto imageCreateDto);

    ImageResponseDto imageToImageResponseDto(Image image);
}
