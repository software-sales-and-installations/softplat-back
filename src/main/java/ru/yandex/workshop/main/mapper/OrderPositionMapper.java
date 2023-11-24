package ru.yandex.workshop.main.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.yandex.workshop.main.dto.basket.OrderPositionResponseDto;
import ru.yandex.workshop.main.model.buyer.BasketPosition;
import ru.yandex.workshop.main.model.buyer.OrderPosition;

@Mapper(uses = ProductMapper.class)
public interface OrderPositionMapper {

    @Mapping(target = "id", ignore = true)
    OrderPosition basketPositionToOrderPosition(BasketPosition productBasket);

    @Mapping(target = "productResponseDto", source = "product")
    OrderPositionResponseDto orderPositionToDto(OrderPosition productOrder);
}
