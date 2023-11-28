package ru.softplat.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.softplat.dto.basket.OrderPositionResponseDto;
import ru.softplat.dto.stats.StatsCreateDto;
import ru.softplat.model.buyer.BasketPosition;
import ru.softplat.model.buyer.OrderPosition;

@Mapper(uses = ProductMapper.class)
public interface OrderPositionMapper {

    @Mapping(target = "id", ignore = true)
    OrderPosition basketPositionToOrderPosition(BasketPosition productBasket);

    @Mapping(target = "productResponseDto", source = "product")
    OrderPositionResponseDto orderPositionToDto(OrderPosition productOrder);

    StatsCreateDto orderPositionToStatDto(OrderPosition orderPosition);
}
