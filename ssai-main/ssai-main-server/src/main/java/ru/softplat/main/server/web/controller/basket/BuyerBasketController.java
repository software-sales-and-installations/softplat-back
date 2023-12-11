package ru.softplat.main.server.web.controller.basket;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.softplat.main.dto.basket.BasketResponseDto;
import ru.softplat.main.server.mapper.BasketMapper;
import ru.softplat.main.server.model.buyer.Basket;
import ru.softplat.main.server.service.buyer.BasketService;

@RestController
@RequestMapping(path = "/basket")
@RequiredArgsConstructor
public class BuyerBasketController {
    private final BasketService basketService;
    private final BasketMapper basketMapper;

    @PostMapping("/{productId}")
    @ResponseStatus(value = HttpStatus.CREATED)
    public BasketResponseDto addProductInBasket(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @PathVariable Long productId,
            @RequestParam Boolean installation) {
        Basket response = basketService.addProduct(userId, productId, installation);
        return basketMapper.basketToBasketResponseDto(response);
    }

    @DeleteMapping("/product/{productId}")
    public BasketResponseDto removeProductFromBasket(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @PathVariable Long productId,
            @RequestParam Boolean installation) {
        Basket response = basketService.removeProduct(userId, productId, installation);
        return basketMapper.basketToBasketResponseDto(response);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void clearBasket(@RequestHeader("X-Sharer-User-Id") long userId) {
        basketService.clearBasket(userId);
    }

    @DeleteMapping("/{basketPositionId}")
    public BasketResponseDto removeBasketPosition(
            @RequestHeader("X-Sharer-User-Id") long userId,
            @PathVariable long basketPositionId) {
        Basket response = basketService.removePositionFromBasket(userId, basketPositionId);
        return basketMapper.basketToBasketResponseDto(response);
    }

    @GetMapping
    public BasketResponseDto getBasket(@RequestHeader("X-Sharer-User-Id") long userId) {
        Basket response = basketService.getOrCreateBasket(userId);
        return basketMapper.basketToBasketResponseDto(response);
    }
}
