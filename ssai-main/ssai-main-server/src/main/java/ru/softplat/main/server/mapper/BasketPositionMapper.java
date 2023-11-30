package ru.softplat.main.server.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;
import ru.softplat.main.server.model.buyer.BasketPosition;
import ru.softplat.dto.basket.BasketPositionResponseDto;


@Mapper(uses = ProductMapper.class)
@Component
public interface BasketPositionMapper {
    @Mapping(target = "productResponseDto", source = "product")
    BasketPositionResponseDto basketPositionToDto(BasketPosition basketPosition);
}