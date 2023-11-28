package ru.softplat.web.controller.basket;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.yandex.workshop.main.dto.basket.BasketResponseDto;
import ru.yandex.workshop.main.mapper.BasketMapper;
import ru.yandex.workshop.main.message.LogMessage;
import ru.yandex.workshop.main.model.buyer.Basket;
import ru.yandex.workshop.main.service.buyer.BasketService;
import springfox.documentation.annotations.ApiIgnore;

import java.security.Principal;

@Slf4j
@RestController
@RequestMapping(path = "/buyer/basket")
@RequiredArgsConstructor
public class BuyerBasketController {
    private final BasketService basketService;
    private final BasketMapper basketMapper;

    @Operation(summary = "Добавление продукта в свою корзину", description = "Доступ для покупателя")
    @PreAuthorize("hasAuthority('buyer:write')")
    @PostMapping("/{productId}")
    @ResponseStatus(value = HttpStatus.CREATED)
    public BasketResponseDto addProductInBasket(@ApiIgnore Principal principal, @PathVariable Long productId,
                                                @RequestParam(defaultValue = "false") Boolean installation) {
        log.debug(LogMessage.TRY_ADD_PRODUCT_IN_BASKET.label, productId);
        Basket response = basketService.addProduct(principal.getName(), productId, installation);
        return basketMapper.basketToBasketResponseDto(response);
    }

    @Operation(summary = "Удаление продукта из своей корзины", description = "Доступ для покупателя")
    @PreAuthorize("hasAuthority('buyer:write')")
    @DeleteMapping("/{productId}")
    public BasketResponseDto removeProductFromBasket(@ApiIgnore Principal principal, @PathVariable Long productId,
                                                     @Parameter(description = "Так как в корзине может лежать два " +
                                                             "одинаковых товара, но один с установкой, а второй без установки, " +
                                                             "необходимо выбрать, какой из них удалить")
                                                     @RequestParam(defaultValue = "false") Boolean installation) {
        log.info(LogMessage.TRY_DELETE_PRODUCT_FROM_BASKET.label, productId);
        Basket response = basketService.removeProduct(principal.getName(), productId, installation);
        return basketMapper.basketToBasketResponseDto(response);
    }

    @Operation(summary = "Просмотр своей корзины", description = "Доступ для покупателя")
    @PreAuthorize("hasAuthority('buyer:write')")
    @GetMapping
    public BasketResponseDto getBasket(@ApiIgnore Principal principal) {
        log.debug(LogMessage.TRY_CHECK_BASKET.label, principal.getName());
        Basket response = basketService.getOrCreateBasket(principal.getName());
        return basketMapper.basketToBasketResponseDto(response);
    }
}
