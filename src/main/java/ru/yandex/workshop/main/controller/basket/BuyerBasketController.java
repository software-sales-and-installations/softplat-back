package ru.yandex.workshop.main.controller.basket;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.workshop.main.dto.basket.BasketDto;
import ru.yandex.workshop.main.message.LogMessage;
import ru.yandex.workshop.main.service.buyer.BasketService;

@Slf4j
@RestController("/buyer")
@RequiredArgsConstructor
public class BuyerBasketController {
    private final BasketService basketService;

    @PostMapping("/{userId}/basket/{productId}")
    public BasketDto addProductInBasket(@PathVariable Long userId, @PathVariable Long productId,
                                        @RequestParam(defaultValue = "false") Boolean installation) {
        log.info(LogMessage.TRY_ADD_PRODUCT_IN_BASKET.label, productId);
        return basketService.addProduct(userId, productId, installation);
    }

    @DeleteMapping("/{userId}/basket/{productId}")
    public BasketDto removeProductFromBasket(@PathVariable Long userId, @PathVariable Long productId,
                                             @RequestParam(defaultValue = "false") Boolean installation) {
        log.info(LogMessage.TRY_DELETE_PRODUCT_FROM_BASKET.label, productId);
        return basketService.removeProduct(userId, productId, installation);
    }

    @GetMapping("/{userId}/basket")
    public BasketDto getBasket(@PathVariable Long userId) {
        log.info(LogMessage.TRY_CHECK_BASKET.label, userId);
        return basketService.getBasket(userId);
    }
}
