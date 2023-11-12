package ru.yandex.workshop.main.controller.basket;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.yandex.workshop.main.dto.basket.BasketDto;
import ru.yandex.workshop.main.message.LogMessage;
import ru.yandex.workshop.main.service.buyer.BasketService;

import java.security.Principal;

@Slf4j
@RestController("/buyer")
@RequiredArgsConstructor
public class BuyerBasketController {
    private final BasketService basketService;

    @Operation(summary = "Добавление продукта в свою корзину", description = "Доступ для покупателя")
    @PreAuthorize("hasAuthority('buyer:write')")
    @PostMapping("/basket/{productId}")
    public BasketDto addProductInBasket(Principal principal, @PathVariable Long productId,
                                        @RequestParam(defaultValue = "false") Boolean installation) {
        log.info(LogMessage.TRY_ADD_PRODUCT_IN_BASKET.label, productId);
        return basketService.addProduct(principal.getName(), productId, installation);
    }

    @Operation(summary = "Удаление продукта из своей корзины", description = "Доступ для покупателя")
    @PreAuthorize("hasAuthority('buyer:write')")
    @DeleteMapping("/basket/{productId}")
    public BasketDto removeProductFromBasket(Principal principal, @PathVariable Long productId,
                                             @Parameter(description = "Так как в корзине может лежать два " +
                                                     "одинаковых товара, но один с установкой, а второй без установки, " +
                                                     "необходимо выбрать, какой из них удалить")
                                             @RequestParam(defaultValue = "false") Boolean installation) {
        log.info(LogMessage.TRY_DELETE_PRODUCT_FROM_BASKET.label, productId);
        return basketService.removeProduct(principal.getName(), productId, installation);
    }

    @Operation(summary = "Просмотр своей корзины", description = "Доступ для покупателя")
    @PreAuthorize("hasAuthority('buyer:write')")
    @GetMapping("/basket")
    public BasketDto getBasket(Principal principal) {
        log.info(LogMessage.TRY_CHECK_BASKET.label, principal.getName());
        return basketService.getBasket(principal.getName());
    }
}
