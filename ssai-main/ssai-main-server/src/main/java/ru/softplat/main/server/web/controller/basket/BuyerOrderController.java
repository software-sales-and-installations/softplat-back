package ru.softplat.main.server.web.controller.basket;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.softplat.main.dto.basket.OrderCreateDto;
import ru.softplat.main.dto.basket.OrderResponseDto;
import ru.softplat.main.dto.basket.OrdersListResponseDto;
import ru.softplat.main.server.exception.WrongConditionException;
import ru.softplat.main.server.mapper.OrderMapper;
import ru.softplat.main.server.model.buyer.Order;
import ru.softplat.main.server.service.buyer.OrderService;
import ru.softplat.main.server.service.email.EmailService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/orders")
@RequiredArgsConstructor
public class BuyerOrderController {
    private final OrderService orderService;
    private final OrderMapper orderMapper;
    private final EmailService emailService;

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public OrderResponseDto addOrder(@RequestHeader("X-Sharer-User-Id") long userId, @RequestBody OrderCreateDto orderCreateDto) {
        Order response = orderService.createOrder(userId, orderCreateDto.getBasketPositionIds());
        emailService.sendOrderConfirmationEmails(response);
        return orderMapper.orderToOrderDto(response);
    }

    @GetMapping
    public OrdersListResponseDto getAllOrders(@RequestHeader("X-Sharer-User-Id") long userId) {
        List<OrderResponseDto> response = orderService.getAllOrders(userId).stream()
                .map(orderMapper::orderToOrderDto)
                .collect(Collectors.toList());
        return orderMapper.toOrdersListResponseDto(response);
    }

    @GetMapping("/{orderId}")
    public OrderResponseDto getOrder(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable long orderId) {
        Order response = orderService.getOrder(orderId);
        if (!response.getBuyer().getId().equals(userId))
            throw new WrongConditionException("Ошибка при выполнении запроса. Попробуйте ввести корректный orderId");
        return orderMapper.orderToOrderDto(response);
    }
}