package ru.yandex.workshop.main.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;
import ru.yandex.workshop.main.dto.basket.BasketPositionResponseDto;
import ru.yandex.workshop.main.model.buyer.BasketPosition;

@Mapper(componentModel = "spring", uses = ProductMapper.class)
@Component
public interface BasketPositionMapper {
    @Mapping(target = "productResponseDto", source = "product")
    BasketPositionResponseDto basketPositionToDto(BasketPosition basketPosition);
}