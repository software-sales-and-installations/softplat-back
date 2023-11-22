package ru.yandex.workshop.main.mapper;

import org.mapstruct.Mapper;
import ru.yandex.workshop.main.dto.basket.OrderResponseDto;
import ru.yandex.workshop.main.dto.basket.OrdersListResponseDto;
import ru.yandex.workshop.main.model.buyer.Order;

import java.util.List;

@Mapper(uses = OrderPositionMapper.class)
public interface OrderMapper {

    OrderResponseDto orderToOrderDto(Order order);

    default OrdersListResponseDto toOrdersListResponseDto(List<OrderResponseDto> orders) {
        return OrdersListResponseDto.builder().orders(orders).build();
    }
}
