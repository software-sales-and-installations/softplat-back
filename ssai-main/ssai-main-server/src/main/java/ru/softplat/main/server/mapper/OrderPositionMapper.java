package ru.softplat.main.server.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.softplat.main.dto.basket.OrderPositionResponseDto;
import ru.softplat.main.server.model.buyer.BasketPosition;
import ru.softplat.main.server.model.buyer.OrderPosition;

@Mapper(uses = ProductMapper.class)
public interface OrderPositionMapper {

    @Mapping(target = "id", ignore = true)
    OrderPosition basketPositionToOrderPosition(BasketPosition productBasket);

    @Mapping(target = "productResponseDto", source = "product")
    OrderPositionResponseDto orderPositionToDto(OrderPosition productOrder);
}
