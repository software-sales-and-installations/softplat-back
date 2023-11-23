package ru.yandex.workshop.main.web.controller.user;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.workshop.main.dto.user.BuyerUpdateDto;
import ru.yandex.workshop.main.dto.user.response.BuyerResponseDto;
import ru.yandex.workshop.main.dto.user.response.BuyersListResponseDto;
import ru.yandex.workshop.main.dto.user.response.FavoriteResponseDto;
import ru.yandex.workshop.main.dto.user.response.FavouritesListResponseDto;
import ru.yandex.workshop.main.mapper.BuyerMapper;
import ru.yandex.workshop.main.mapper.FavoriteMapper;
import ru.yandex.workshop.main.message.LogMessage;
import ru.yandex.workshop.main.model.buyer.Buyer;
import ru.yandex.workshop.main.model.buyer.Favorite;
import ru.yandex.workshop.main.service.buyer.BuyerFavoriteService;
import ru.yandex.workshop.main.service.buyer.BuyerService;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/buyer")
public class BuyerController {
    private final BuyerService buyerService;
    private final BuyerFavoriteService favoriteService;
    private final BuyerMapper buyerMapper;
    private final FavoriteMapper favoriteMapper;

    @Operation(summary = "Получение списка покупателей", description = "Доступ для админа")
    @PreAuthorize("hasAuthority('admin:write')")
    @GetMapping
    public BuyersListResponseDto getAllBuyers(@RequestParam(name = "minId", defaultValue = "0") @Min(0) int minId,
                                              @RequestParam(name = "pageSize", defaultValue = "20") @Min(1) int pageSize) {
        log.debug(LogMessage.TRY_GET_All_BUYERS.label);
        List<Buyer> buyerList = buyerService.getAllBuyers(minId, pageSize);
        List<BuyerResponseDto> response = buyerList.stream()
                .map(buyerMapper::buyerToBuyerResponseDto)
                .collect(Collectors.toList());
        return buyerMapper.toBuyersListResponseDto(response);
    }

    @PreAuthorize("hasAuthority('buyer:write') || hasAuthority('admin:write')")
    @Operation(summary = "Получение покупателя по id", description = "Доступ для покупателя и админа")
    @GetMapping("/{userId}")
    public BuyerResponseDto getBuyer(@PathVariable Long userId) {
        log.info(LogMessage.TRY_GET_BUYER.label, userId);
        Buyer response = buyerService.getBuyer(userId);
        return buyerMapper.buyerToBuyerResponseDto(response);
    }

    @Operation(summary = "Обновление данных о себе покупателем", description = "Доступ для покупателя")
    @PreAuthorize("hasAuthority('buyer:write')")
    @PatchMapping
    public BuyerResponseDto updateBuyer(@ApiIgnore Principal principal, @RequestBody @Valid BuyerUpdateDto buyerUpdateDto) {
        log.info(LogMessage.TRY_PATCH_BUYER.label, principal.getName());
        Buyer updateRequest = buyerMapper.buyerDtoToBuyer(buyerUpdateDto);
        Buyer response = buyerService.updateBuyer(principal.getName(), updateRequest);
        return buyerMapper.buyerToBuyerResponseDto(response);
    }

    @Operation(summary = "Добавление товара в избранное", description = "Доступ для покупателя")
    @PreAuthorize("hasAuthority('buyer:write')")
    @PostMapping("/favorites/{productId}")
    @ResponseStatus(value = HttpStatus.CREATED)
    public FavoriteResponseDto createFavorite(@ApiIgnore Principal principal, @PathVariable Long productId) {
        log.info(LogMessage.TRY_BUYER_ADD_FAVORITE.label, "{}, {}", principal.getName(), productId);
        Favorite response = favoriteService.create(principal.getName(), productId);
        return favoriteMapper.toFavouriteDto(response);
    }

    @Operation(summary = "Удаление товара из избранного", description = "Доступ для покупателя")
    @PreAuthorize("hasAuthority('buyer:write')")
    @DeleteMapping("/favorites/{productId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteFavorite(@ApiIgnore Principal principal,
                               @PathVariable Long productId) {
        log.info(LogMessage.TRY_BUYER_DELETE_FAVORITE.label, "{}, {}", principal.getName(), productId);
        favoriteService.delete(principal.getName(), productId);
    }

    @Operation(summary = "Просмотр избранных товаров", description = "Доступ для покупателя")
    @PreAuthorize("hasAuthority('buyer:write')")
    @GetMapping("/favorites")
    public FavouritesListResponseDto getBuyerFavouriteProducts(@ApiIgnore Principal principal) {
        log.info(LogMessage.TRY_BUYER_GET_FAVORITE.label, principal.getName());
        List<Favorite> favoriteList = favoriteService.getAll(principal.getName());
        List<FavoriteResponseDto> response = favoriteList.stream()
                .map(favoriteMapper::toFavouriteDto)
                .collect(Collectors.toList());
        return favoriteMapper.toFavouritesListResponseDto(response);
    }
}
