package ru.yandex.workshop.main.web.controller.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.workshop.main.dto.user.BuyerDto;
import ru.yandex.workshop.main.dto.user.response.BuyerResponseDto;
import ru.yandex.workshop.main.dto.user.response.FavoriteDto;
import ru.yandex.workshop.main.mapper.BuyerMapper;
import ru.yandex.workshop.main.mapper.FavoriteMapper;
import ru.yandex.workshop.main.message.LogMessage;
import ru.yandex.workshop.main.model.buyer.Buyer;
import ru.yandex.workshop.main.model.buyer.Favorite;
import ru.yandex.workshop.main.service.buyer.BuyerFavoriteService;
import ru.yandex.workshop.main.service.buyer.BuyerService;

import javax.validation.Valid;
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

    @GetMapping("/{userId}")
    public BuyerResponseDto getBuyer(@PathVariable Long userId) {
        log.info(LogMessage.TRY_GET_BUYER.label, userId);
        Buyer response = buyerService.getBuyer(userId);
        return buyerMapper.buyerToBuyerResponseDto(response);
    }

    @PreAuthorize("hasAuthority('buyer:write')")
    @PatchMapping
    public BuyerResponseDto updateBuyer(Principal principal, @RequestBody @Valid BuyerDto buyerDto) {
        log.info(LogMessage.TRY_PATCH_BUYER.label, principal.getName());
        Buyer updateRequest = buyerMapper.buyerDtoToBuyer(buyerDto);
        Buyer response = buyerService.updateBuyer(principal.getName(), updateRequest);
        return buyerMapper.buyerToBuyerResponseDto(response);
    }

    @PreAuthorize("hasAuthority('buyer:write')")
    @PostMapping("/favorites/{productId}")
    public FavoriteDto createFavorite(Principal principal, @PathVariable Long productId) {
        log.info(LogMessage.TRY_BUYER_ADD_FAVORITE.label, "{}, {}", principal.getName(), productId);
        Favorite response = favoriteService.create(principal.getName(), productId);
        return favoriteMapper.toFavouriteDto(response);
    }

    @PreAuthorize("hasAuthority('buyer:write')")
    @DeleteMapping("/favorites/{productId}")
    public void deleteFavorite(Principal principal, @PathVariable Long productId) {
        log.info(LogMessage.TRY_BUYER_DELETE_FAVORITE.label, "{}, {}", principal.getName(), productId);
        favoriteService.delete(principal.getName(), productId);
    }

    @PreAuthorize("hasAuthority('buyer:write')")
    @GetMapping("/favorites")
    public List<FavoriteDto> getAll(Principal principal) {
        log.info(LogMessage.TRY_BUYER_GET_FAVORITE.label, principal.getName());
        List<Favorite> response = favoriteService.getAll(principal.getName());
        return response.stream()
                .map(favoriteMapper::toFavouriteDto)
                .collect(Collectors.toList());
    }
}
