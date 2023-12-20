package ru.softplat.main.server.web.controller.basket;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.softplat.main.dto.basket.BasketCreateDto;
import ru.softplat.main.dto.basket.BasketResponseDto;
import ru.softplat.main.server.mapper.BasketMapper;
import ru.softplat.main.server.mapper.BasketPositionMapper;
import ru.softplat.main.server.model.buyer.Basket;
import ru.softplat.main.server.model.buyer.BasketPosition;
import ru.softplat.main.server.model.product.Product;
import ru.softplat.main.server.service.buyer.BasketService;
import ru.softplat.main.server.service.product.ProductService;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path = "/basket")
@RequiredArgsConstructor
public class BuyerBasketController {
    private final BasketService basketService;
    private final ProductService productService;
    private final BasketMapper basketMapper;
    private final BasketPositionMapper basketPositionMapper;

    @PostMapping("/{productId}")
    @ResponseStatus(value = HttpStatus.CREATED)
    public BasketResponseDto addProductInBasket(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable Long productId,
                                                @RequestParam Boolean installation) {
        Basket response = basketService.addProduct(userId, productId, installation);
        return basketMapper.basketToBasketResponseDto(response);
    }

    @DeleteMapping("/product/{productId}")
    public BasketResponseDto removeProductFromBasket(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable Long productId,
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
    public BasketResponseDto removeBasketPosition(@RequestHeader("X-Sharer-User-Id") long userId,
                                                  @PathVariable long basketPositionId) {
        Basket response = basketService.removePositionFromBasket(userId, basketPositionId);
        return basketMapper.basketToBasketResponseDto(response);
    }

    @GetMapping
    public BasketResponseDto getBasket(@RequestHeader("X-Sharer-User-Id") long userId) {
        Basket response = basketService.getOrCreateBasket(userId);
        return basketMapper.basketToBasketResponseDto(response);
    }

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public BasketResponseDto saveBasket(@RequestHeader("X-Sharer-User-Id") long userId,
                                        @RequestBody BasketCreateDto basketCreateDto) {
        List<BasketPosition> basketPositions = new ArrayList<>();
        for (int i = 0; i < basketCreateDto.getProductsInBasket().size(); i++) {
            Product product = productService.getAvailableProduct(basketCreateDto.getProductsInBasket().get(i).getProductId());
            BasketPosition position = basketPositionMapper.createBasketPosition(
                    basketCreateDto.getProductsInBasket().get(i), product);
            basketPositions.add(position);
        }
        Basket response = basketService.saveBasket(userId, basketPositions);
        return basketMapper.basketToBasketResponseDto(response);
    }
}
