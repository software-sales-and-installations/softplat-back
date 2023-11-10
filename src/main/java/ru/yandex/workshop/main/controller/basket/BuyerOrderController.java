package ru.yandex.workshop.main.controller.basket;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.yandex.workshop.main.dto.basket.OrderResponseDto;
import ru.yandex.workshop.main.dto.basket.OrderToCreateDto;
import ru.yandex.workshop.main.message.LogMessage;
import ru.yandex.workshop.main.service.buyer.OrderService;

import java.security.Principal;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/orders")
@RequiredArgsConstructor
public class BuyerOrderController {
    private final OrderService orderService;

    @PreAuthorize("hasAuthority('buyer:write')")
    @PostMapping
    public OrderResponseDto addOrder(Principal principal, @RequestBody OrderToCreateDto orderToCreateDto) {
        log.debug(LogMessage.TRY_ADD_ORDER.label);
        return orderService.createOrder(principal.getName(), orderToCreateDto);
    }

    @PreAuthorize("hasAuthority('buyer:write')")
    @GetMapping
    public List<OrderResponseDto> getAllOrders(Principal principal) {
        log.debug(LogMessage.TRY_GET_ALL_ORDERS.label);
        return orderService.getAllOrders(principal.getName());
    }

    @PreAuthorize("hasAuthority('buyer:write')")
    @GetMapping("/{orderId}")
    public OrderResponseDto getOrder(Principal principal, @PathVariable long orderId) {
        log.debug(LogMessage.TRY_ADD_ORDER.label, orderId);
        return orderService.getOrder(principal.getName(), orderId);
    }
}
