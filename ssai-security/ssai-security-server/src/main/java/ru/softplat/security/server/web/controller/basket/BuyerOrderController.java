package ru.softplat.security.server.web.controller.basket;

import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
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
import ru.softplat.main.dto.basket.OrderResponseDto;
import ru.softplat.main.dto.basket.OrdersListResponseDto;
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

    @ApiResponses(value = {@ApiResponse(code = 201, message = "Created", response = OrderResponseDto.class)})
    @Operation(summary = "Оформление заказа", description = "Доступ для покупателя")
    @PreAuthorize("hasAuthority('buyer:write')")
    @PostMapping(produces = "application/json")
    @ResponseStatus(value = HttpStatus.CREATED)
    public ResponseEntity<Object> addOrder(@RequestHeader("X-Sharer-User-Id") long userId, @RequestBody @Valid OrderCreateDto orderCreateDto) {
        log.debug(LogMessage.TRY_ADD_ORDER.label);
        if (orderCreateDto.getBasketPositionIds() == null || orderCreateDto.getBasketPositionIds().size() == 0)
            throw new EntityNotFoundException("Перед оформлением заказа добавьте товары в корзину");
        return orderClient.addOrder(userId, orderCreateDto);
    }

    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK", response = OrdersListResponseDto.class)})
    @Operation(summary = "Список: Мои покупки", description = "Доступ для покупателя")
    @PreAuthorize("hasAuthority('buyer:write')")
    @GetMapping(produces = "application/json")
    public ResponseEntity<Object> getAllOrders(@RequestHeader("X-Sharer-User-Id") long userId) {
        log.debug(LogMessage.TRY_GET_ALL_ORDERS.label);
        return orderClient.getAllOrders(userId);
    }

    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK", response = OrderResponseDto.class)})
    @Operation(summary = "Получение конкретной покупки", description = "Доступ для покупателя")
    @PreAuthorize("hasAuthority('buyer:write')")
    @GetMapping(path = "/{orderId}", produces = "application/json")
    public ResponseEntity<Object> getOrder(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable long orderId) {
        log.debug(LogMessage.TRY_GET_ORDER.label, orderId);
        return orderClient.getOrder(userId, orderId);
    }
}