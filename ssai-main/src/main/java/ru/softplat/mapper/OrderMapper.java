package ru.softplat.mapper;

import org.mapstruct.Mapper;
import ru.softplat.dto.basket.OrderResponseDto;
import ru.softplat.dto.basket.OrdersListResponseDto;
import ru.softplat.model.buyer.Order;

import java.util.List;

@Mapper(uses = OrderPositionMapper.class)
public interface OrderMapper {

    OrderResponseDto orderToOrderDto(Order order);

    default OrdersListResponseDto toOrdersListResponseDto(List<OrderResponseDto> orders) {
        return OrdersListResponseDto.builder().orders(orders).build();
    }
}
