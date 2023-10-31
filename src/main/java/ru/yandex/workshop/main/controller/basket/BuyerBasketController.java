package ru.yandex.workshop.main.controller.basket;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.workshop.main.dto.basket.BasketDto;
import ru.yandex.workshop.main.service.buyer.BasketService;

@RestController("/buyer")
@RequiredArgsConstructor
public class BuyerBasketController {
    private final BasketService basketService;

    @PostMapping("/{userId}/basket/{productId}")
    public BasketDto addProductInBasket(@PathVariable Long userId, @PathVariable Long productId) {
        //TODO logger
        return basketService.addProduct(userId, productId);
    }

    @DeleteMapping("/{userId}/basket/{productId}")
    public BasketDto removeProductFromBasket(@PathVariable Long userId, @PathVariable Long productId) {
        //TODO logger
        return basketService.removeProduct(userId, productId);
    }

    @GetMapping("/{userId}/basket")
    public BasketDto getBasket(@PathVariable Long userId) {
        //TODO logger
        return basketService.getBasket(userId);
    }
}
