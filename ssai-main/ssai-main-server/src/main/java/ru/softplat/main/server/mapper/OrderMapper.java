package ru.softplat.main.server.mapper;

import org.mapstruct.Mapper;
import ru.softplat.main.dto.basket.OrderResponseDto;
import ru.softplat.main.dto.basket.OrdersListResponseDto;
import ru.softplat.main.server.model.buyer.Order;

import java.util.List;

@Mapper(uses = OrderPositionMapper.class)
public interface OrderMapper {

    OrderResponseDto orderToOrderDto(Order order);

    default OrdersListResponseDto toOrdersListResponseDto(List<OrderResponseDto> orders) {
        return OrdersListResponseDto.builder().orders(orders).build();
    }
}
