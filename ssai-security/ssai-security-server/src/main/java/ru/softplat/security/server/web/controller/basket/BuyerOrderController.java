package ru.softplat.security.server.web.controller.basket;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.softplat.main.client.basket.OrderClient;
import ru.softplat.main.dto.basket.OrderCreateDto;
import ru.softplat.security.server.message.LogMessage;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;

@Slf4j
@Validated
@RestController
@RequestMapping(path = "/orders")
@RequiredArgsConstructor
public class BuyerOrderController {
    private final OrderClient orderClient;

    @Operation(summary = "Оформление заказа", description = "Доступ для покупателя")
    @PreAuthorize("hasAuthority('buyer:write')")
    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public ResponseEntity<Object> addOrder(@RequestHeader("X-Sharer-User-Id") long userId, @RequestBody @Valid OrderCreateDto orderCreateDto) {
        log.debug(LogMessage.TRY_ADD_ORDER.label);
        if (orderCreateDto.getBasketPositionIds() == null || orderCreateDto.getBasketPositionIds().size() == 0)
            throw new EntityNotFoundException("Перед оформлением заказа добавьте товары в корзину");
        return orderClient.addOrder(userId, orderCreateDto);
    }

    @Operation(summary = "Список: Мои покупки", description = "Доступ для покупателя")
    @PreAuthorize("hasAuthority('buyer:write')")
    @GetMapping
    public ResponseEntity<Object> getAllOrders(@RequestHeader("X-Sharer-User-Id") long userId) {
        log.debug(LogMessage.TRY_GET_ALL_ORDERS.label);
        return orderClient.getAllOrders(userId);
    }

    @Operation(summary = "Получение конкретной покупки", description = "Доступ для покупателя")
    @PreAuthorize("hasAuthority('buyer:write')")
    @GetMapping("/{orderId}")
    public ResponseEntity<Object> getOrder(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable long orderId) {
        log.debug(LogMessage.TRY_GET_ORDER.label, orderId);
        return orderClient.getOrder(userId, orderId);
    }
}