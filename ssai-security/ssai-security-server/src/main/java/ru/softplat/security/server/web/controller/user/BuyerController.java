package ru.softplat.security.server.web.controller.user;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.softplat.main.client.user.BuyerClient;
import ru.softplat.main.dto.user.BuyerUpdateDto;
import ru.softplat.security.server.message.LogMessage;

import javax.validation.Valid;
import javax.validation.constraints.Min;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/buyer")
public class BuyerController {
    private final BuyerClient buyerClient;

    @Operation(summary = "Получение списка покупателей", description = "Доступ для админа")
    @PreAuthorize("hasAuthority('admin:write')")
    @GetMapping
    public ResponseEntity<Object> getAllBuyers(@RequestParam(name = "minId", defaultValue = "0") @Min(0) int minId,
                                               @RequestParam(name = "pageSize", defaultValue = "20") @Min(1) int pageSize) {
        log.debug(LogMessage.TRY_GET_All_BUYERS.label);
        return buyerClient.getAllBuyers(minId, pageSize);
    }

    @PreAuthorize("hasAuthority('buyer:write') || hasAuthority('admin:write')")
    @Operation(summary = "Получение покупателя по id", description = "Доступ для покупателя и админа")
    @GetMapping("/{userId}")
    public ResponseEntity<Object> getBuyer(@PathVariable Long userId) {
        log.info(LogMessage.TRY_GET_BUYER.label, userId);
        return buyerClient.getBuyer(userId);
    }

    @Operation(summary = "Обновление данных о себе покупателем", description = "Доступ для покупателя")
    @PreAuthorize("hasAuthority('buyer:write')")
    @PatchMapping
    public ResponseEntity<Object> updateBuyer(@RequestHeader long userId, @RequestBody @Valid BuyerUpdateDto buyerUpdateDto) {
        log.info(LogMessage.TRY_PATCH_BUYER.label, userId);
        return buyerClient.updateBuyer(userId, buyerUpdateDto);
    }

    @Operation(summary = "Добавление товара в избранное", description = "Доступ для покупателя")
    @PreAuthorize("hasAuthority('buyer:write')")
    @PostMapping("/favorites/{productId}")
    @ResponseStatus(value = HttpStatus.CREATED)
    public ResponseEntity<Object> createFavorite(@RequestHeader long userId, @PathVariable Long productId) {
        log.info(LogMessage.TRY_BUYER_ADD_FAVORITE.label, "{}, {}", userId, productId);
        return buyerClient.createFavourite(userId, productId);
    }

    @Operation(summary = "Удаление товара из избранного", description = "Доступ для покупателя")
    @PreAuthorize("hasAuthority('buyer:write')")
    @DeleteMapping("/favorites/{productId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteFavorite(@RequestHeader long userId,
                               @PathVariable Long productId) {
        log.info(LogMessage.TRY_BUYER_DELETE_FAVORITE.label, "{}, {}", userId, productId);
        buyerClient.deleteFavourite(userId, productId);
    }

    @Operation(summary = "Просмотр избранных товаров", description = "Доступ для покупателя")
    @PreAuthorize("hasAuthority('buyer:write')")
    @GetMapping("/favorites")
    public ResponseEntity<Object> getBuyerFavouriteProducts(@RequestHeader long userId) {
        log.info(LogMessage.TRY_BUYER_GET_FAVORITE.label, userId);
        return buyerClient.getFavourites(userId);
    }
}
