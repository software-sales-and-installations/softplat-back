package ru.softplat.main.server.mapper;

import org.mapstruct.Mapper;
import ru.softplat.main.server.model.buyer.Order;
import ru.softplat.dto.basket.OrderResponseDto;
import ru.softplat.dto.basket.OrdersListResponseDto;

import java.util.List;

@Mapper(uses = OrderPositionMapper.class)
public interface OrderMapper {

    OrderResponseDto orderToOrderDto(Order order);

    default OrdersListResponseDto toOrdersListResponseDto(List<OrderResponseDto> orders) {
        return OrdersListResponseDto.builder().orders(orders).build();
    }
}
