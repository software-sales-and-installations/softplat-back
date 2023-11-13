package ru.yandex.workshop.main.web.controller.basket;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.yandex.workshop.main.dto.basket.BasketDto;
import ru.yandex.workshop.main.mapper.BasketMapper;
import ru.yandex.workshop.main.message.LogMessage;
import ru.yandex.workshop.main.model.buyer.Basket;
import ru.yandex.workshop.main.service.buyer.BasketService;

import java.security.Principal;

@Slf4j
@RestController("/buyer")
@RequiredArgsConstructor
public class BuyerBasketController {
    private final BasketService basketService;
    private final BasketMapper basketMapper;

    @PreAuthorize("hasAuthority('buyer:write')")
    @PostMapping("/basket/{productId}")
    public BasketDto addProductInBasket(Principal principal, @PathVariable Long productId,
                                        @RequestParam(defaultValue = "false") Boolean installation) {
        log.info(LogMessage.TRY_ADD_PRODUCT_IN_BASKET.label, productId);
        Basket response = basketService.addProduct(principal.getName(), productId, installation);
        return basketMapper.basketToBasketDto(response);
    }

    @PreAuthorize("hasAuthority('buyer:write')")
    @DeleteMapping("/basket/{productId}")
    public BasketDto removeProductFromBasket(Principal principal, @PathVariable Long productId,
                                             @RequestParam(defaultValue = "false") Boolean installation) {
        log.info(LogMessage.TRY_DELETE_PRODUCT_FROM_BASKET.label, productId);
        Basket response = basketService.removeProduct(principal.getName(), productId, installation);
        return basketMapper.basketToBasketDto(response);
    }

    @PreAuthorize("hasAuthority('buyer:write')")
    @GetMapping("/basket")
    public BasketDto getBasket(Principal principal) {
        log.info(LogMessage.TRY_CHECK_BASKET.label, principal.getName());
        Basket response = basketService.getBasket(principal.getName());
        return basketMapper.basketToBasketDto(response);
    }
}
