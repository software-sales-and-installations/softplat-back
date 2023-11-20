package ru.yandex.workshop.main.mapper;

import org.mapstruct.Mapper;
import ru.yandex.workshop.main.dto.basket.OrderResponseDto;
import ru.yandex.workshop.main.model.buyer.Order;

@Mapper(uses = OrderPositionMapper.class)
public interface OrderMapper {

    OrderResponseDto orderToOrderDto(Order order);
}
