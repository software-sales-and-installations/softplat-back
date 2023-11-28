package ru.softplat.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;
import ru.softplat.dto.basket.BasketPositionResponseDto;
import ru.softplat.model.buyer.BasketPosition;


@Mapper(uses = ProductMapper.class)
@Component
public interface BasketPositionMapper {
    @Mapping(target = "productResponseDto", source = "product")
    BasketPositionResponseDto basketPositionToDto(BasketPosition basketPosition);
}