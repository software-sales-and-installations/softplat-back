package ru.softplat.security.server.web.controller.basket;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.softplat.main.client.basket.BasketClient;
import ru.softplat.security.server.message.LogMessage;

@Slf4j
@RestController
@RequestMapping(path = "/basket")
@RequiredArgsConstructor
public class BuyerBasketController {
    private final BasketClient basketClient;

    @Operation(summary = "Добавление продукта в свою корзину", description = "Доступ для покупателя")
    @PreAuthorize("hasAuthority('buyer:write')")
    @PostMapping("/{productId}")
    @ResponseStatus(value = HttpStatus.CREATED)
    public ResponseEntity<Object> addProductInBasket(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable Long productId,
                                                     @RequestParam(defaultValue = "false") Boolean installation) {
        log.debug(LogMessage.TRY_ADD_PRODUCT_IN_BASKET.label, productId);
        return basketClient.addProductInBasket(userId, productId, installation);
    }

    @Operation(summary = "Удаление продукта из своей корзины", description = "Доступ для покупателя")
    @PreAuthorize("hasAuthority('buyer:write')")
    @DeleteMapping("/product/{productId}")
    public ResponseEntity<Object> removeProductFromBasket(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable Long productId,
                                                          @Parameter(description = "Так как в корзине может лежать два " +
                                                                  "одинаковых товара, но один с установкой, а второй без установки, " +
                                                                  "необходимо выбрать, какой из них удалить")
                                                          @RequestParam(defaultValue = "false") Boolean installation) {
        log.info(LogMessage.TRY_DELETE_PRODUCT_FROM_BASKET.label, productId);
        return basketClient.removeProductFromBasket(userId, productId, installation);
    }

    @Operation(summary = "Просмотр своей корзины", description = "Доступ для покупателя")
    @PreAuthorize("hasAuthority('buyer:write')")
    @GetMapping
    public ResponseEntity<Object> getBasket(@RequestHeader("X-Sharer-User-Id") long userId) {
        log.debug(LogMessage.TRY_CHECK_BASKET.label, userId);
        return basketClient.getBasket(userId);
    }

    @Operation(summary = "Очистить корзину", description = "Доступ для покупателя")
    @PreAuthorize("hasAuthority('buyer:write')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping
    public void clearBasket(@RequestHeader("X-Sharer-User-Id") long userId) {
        log.debug(LogMessage.TRY_CLEAR_BASKET.label, userId);
        basketClient.clearBasket(userId);
    }

    @Operation(summary = "Удаление целой позиции корзины", description = "Доступ для покупателя")
    @PreAuthorize("hasAuthority('buyer:write')")
    @DeleteMapping("/{basketPositionId}")
    public ResponseEntity<Object> removeBasketPosition(@RequestHeader("X-Sharer-User-Id") long userId,
                                                  @PathVariable long basketPositionId) {
        log.debug(LogMessage.TRY_DELETE_BASKET_POSITION.label, basketPositionId);
        return basketClient.removeBasketPosition(userId, basketPositionId);
    }
}
