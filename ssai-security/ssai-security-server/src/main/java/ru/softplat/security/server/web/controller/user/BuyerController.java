package ru.softplat.security.server.web.controller.user;

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
import ru.softplat.main.client.user.BuyerClient;
import ru.softplat.main.dto.product.ProductsListResponseDto;
import ru.softplat.main.dto.user.BuyerUpdateDto;
import ru.softplat.main.dto.user.response.BuyerResponseDto;
import ru.softplat.main.dto.user.response.BuyersListResponseDto;
import ru.softplat.main.dto.user.response.FavoriteResponseDto;
import ru.softplat.main.dto.user.response.FavouritesListResponseDto;
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

    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK", response = BuyersListResponseDto.class)})
    @Operation(summary = "Получение списка покупателей", description = "Доступ для админа")
    @PreAuthorize("hasAuthority('admin:write')")
    @GetMapping(produces = "application/json")
    public ResponseEntity<Object> getAllBuyers(@RequestParam(name = "minId", defaultValue = "0") @Min(0) int minId,
                                               @RequestParam(name = "pageSize", defaultValue = "20") @Min(1) int pageSize) {
        log.debug(LogMessage.TRY_GET_All_BUYERS.label);
        return buyerClient.getAllBuyers(minId, pageSize);
    }

    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK", response = BuyerResponseDto.class)})
    @PreAuthorize("hasAuthority('buyer:write') || hasAuthority('admin:write')")
    @Operation(summary = "Получение покупателя по id", description = "Доступ для покупателя и админа")
    @GetMapping(path = "/{userId}", produces = "application/json")
    public ResponseEntity<Object> getBuyer(@PathVariable Long userId) {
        log.info(LogMessage.TRY_GET_BUYER.label, userId);
        return buyerClient.getBuyer(userId);
    }

    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK", response = BuyerResponseDto.class)})
    @Operation(summary = "Обновление данных о себе покупателем", description = "Доступ для покупателя")
    @PreAuthorize("hasAuthority('buyer:write')")
    @PatchMapping(produces = "application/json")
    public ResponseEntity<Object> updateBuyer(@RequestHeader("X-Sharer-User-Id") long userId, @RequestBody @Valid BuyerUpdateDto buyerUpdateDto) {
        log.info(LogMessage.TRY_PATCH_BUYER.label, userId);
        return buyerClient.updateBuyer(userId, buyerUpdateDto);
    }

    @Operation(summary = "Удаление покупателя админом", description = "Доступ для админа")
    @PreAuthorize("hasAuthority('admin:write')")
    @DeleteMapping("/{userId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteBuyer(@PathVariable long userId) {
        log.info(LogMessage.TRY_DELETE_BUYER.label, userId);
        buyerClient.deleteBuyer(userId);
    }

    @ApiResponses(value = {@ApiResponse(code = 201, message = "Created", response = FavoriteResponseDto.class)})
    @Operation(summary = "Добавление товара в избранное", description = "Доступ для покупателя")
    @PreAuthorize("hasAuthority('buyer:write')")
    @PostMapping(path = "/favorites/{productId}", produces = "application/json")
    @ResponseStatus(value = HttpStatus.CREATED)
    public ResponseEntity<Object> createFavorite(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable Long productId) {
        log.info(LogMessage.TRY_BUYER_ADD_FAVORITE.label, "{}, {}", userId, productId);
        return buyerClient.createFavourite(userId, productId);
    }

    @Operation(summary = "Удаление товара из избранного", description = "Доступ для покупателя")
    @PreAuthorize("hasAuthority('buyer:write')")
    @DeleteMapping("/favorites/{productId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteFavorite(@RequestHeader("X-Sharer-User-Id") long userId,
                               @PathVariable Long productId) {
        log.info(LogMessage.TRY_BUYER_DELETE_FAVORITE.label, "{}, {}", userId, productId);
        buyerClient.deleteFavourite(userId, productId);
    }

    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK", response = FavouritesListResponseDto.class)})
    @Operation(summary = "Просмотр избранных товаров", description = "Доступ для покупателя")
    @PreAuthorize("hasAuthority('buyer:write')")
    @GetMapping(path = "/favorites", produces = "application/json")
    public ResponseEntity<Object> getBuyerFavouriteProducts(@RequestHeader("X-Sharer-User-Id") long userId) {
        log.info(LogMessage.TRY_BUYER_GET_FAVORITE.label, userId);
        return buyerClient.getFavourites(userId);
    }

    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK", response = ProductsListResponseDto.class)})
    @Operation(summary = "Просмотр рекомендаций товаров", description = "Доступ для покупателя")
    @PreAuthorize("hasAuthority('buyer:write')")
    @GetMapping(path = "/recommendations", produces = "application/json")
    public ResponseEntity<Object> getProductRecommendations(
            @RequestHeader("X-Sharer-User-Id") long userId,
            @RequestParam(name = "minId", defaultValue = "0") int minId,
            @RequestParam(name = "pageSize", defaultValue = "5") int pageSize
    ) {
        log.info(LogMessage.TRY_BUYER_GET_RECOMMENDATIONS.label, userId);
        return buyerClient.getRecommendations(userId, minId, pageSize);
    }
}
