package ru.yandex.workshop.main.controller.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.workshop.main.dto.buyer.BuyerDto;
import ru.yandex.workshop.main.dto.buyer.BuyerResponseDto;
import ru.yandex.workshop.main.dto.buyer.FavoriteDto;
import ru.yandex.workshop.main.dto.validation.New;
import ru.yandex.workshop.main.message.LogMessage;
import ru.yandex.workshop.main.service.buyer.BuyerFavoriteService;
import ru.yandex.workshop.main.service.buyer.BuyerService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/buyer")
public class BuyerController {

    private final BuyerService buyerService;
    private final BuyerFavoriteService favoriteService;

    @PostMapping
    public BuyerResponseDto addNewBuyer(@RequestBody @Validated(New.class) BuyerDto buyerDto) {
        log.info(LogMessage.TRY_ADD_BUYER.label);
        BuyerResponseDto response = buyerService.addNewBuyer(buyerDto);
        log.info("{}", response);
        return response;
    }

    @GetMapping("/{buyerId}")
    public BuyerResponseDto getBuyerById(@PathVariable(name = "buyerId") long buyerId) {
        log.info(LogMessage.TRY_GET_BUYER.label, buyerId);
        BuyerResponseDto response = buyerService.getBuyer(buyerId);
        log.info("{}", response);
        return response;
    }

    @PatchMapping("/{buyerId}")
    public BuyerResponseDto updateBuyerById(@PathVariable(name = "buyerId") long buyerId,
                                            @RequestBody @Valid BuyerDto buyerDto) {
        log.info(LogMessage.TRY_PATCH_BUYER.label, buyerId);
        BuyerResponseDto response = buyerService.updateBuyer(buyerId, buyerDto);
        log.info("{}", response);
        return response;
    }

    @PostMapping("/{buyerId}/favorites/{productId}")
    public FavoriteDto createFavorite(@PathVariable Long buyerId,
                                      @PathVariable Long productId) {
        log.info(LogMessage.TRY_BUYER_ADD_FAVORITE.label, "{}, {}", buyerId, productId);
        return favoriteService.create(buyerId, productId);
    }

    @DeleteMapping("/{buyerId}/favorites/{productId}")
    public void deleteFavorite(@PathVariable Long buyerId,
                               @PathVariable Long productId) {
        log.info(LogMessage.TRY_BUYER_DELETE_FAVORITE.label, "{}, {}", buyerId, productId);
        favoriteService.delete(buyerId, productId);
    }

    @GetMapping("/{buyerId}/favorites")
    public List<FavoriteDto> getAll(@PathVariable Long buyerId) {
        log.info(LogMessage.TRY_BUYER_GET_FAVORITE.label, buyerId);
        List<FavoriteDto> response = favoriteService.getAll(buyerId);
        log.info("{}", response);
        return response;
    }

}
