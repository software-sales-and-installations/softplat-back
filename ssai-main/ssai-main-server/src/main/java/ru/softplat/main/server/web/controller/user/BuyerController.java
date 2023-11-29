package ru.softplat.main.server.web.controller.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.softplat.main.dto.user.BuyerUpdateDto;
import ru.softplat.main.dto.user.UserCreateMainDto;
import ru.softplat.main.dto.user.response.BuyerResponseDto;
import ru.softplat.main.dto.user.response.BuyersListResponseDto;
import ru.softplat.main.dto.user.response.FavoriteResponseDto;
import ru.softplat.main.dto.user.response.FavouritesListResponseDto;
import ru.softplat.main.server.mapper.BuyerMapper;
import ru.softplat.main.server.mapper.FavoriteMapper;
import ru.softplat.main.server.model.buyer.Buyer;
import ru.softplat.main.server.model.buyer.Favorite;
import ru.softplat.main.server.service.buyer.BuyerFavoriteService;
import ru.softplat.main.server.service.buyer.BuyerService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/buyer")
public class BuyerController {
    private final BuyerService buyerService;
    private final BuyerFavoriteService favoriteService;
    private final BuyerMapper buyerMapper;
    private final FavoriteMapper favoriteMapper;

    @PostMapping
    public BuyerResponseDto createBuyer(@RequestBody UserCreateMainDto userCreateMainDto) {
        Buyer response = buyerService.addBuyer(buyerMapper.userCreateDtoToBuyer(userCreateMainDto));
        return buyerMapper.buyerToBuyerResponseDto(response);
    }

    @GetMapping
    public BuyersListResponseDto getAllBuyers(int minId, int pageSize) {
        List<Buyer> buyerList = buyerService.getAllBuyers(minId, pageSize);
        List<BuyerResponseDto> response = buyerList.stream()
                .map(buyerMapper::buyerToBuyerResponseDto)
                .collect(Collectors.toList());
        return buyerMapper.toBuyersListResponseDto(response);
    }

    @GetMapping("/{userId}")
    public BuyerResponseDto getBuyer(@PathVariable Long userId) {
        Buyer response = buyerService.getBuyer(userId);
        return buyerMapper.buyerToBuyerResponseDto(response);
    }

    @PatchMapping
    public BuyerResponseDto updateBuyer(@RequestHeader long userId, @RequestBody BuyerUpdateDto buyerUpdateDto) {
        Buyer updateRequest = buyerMapper.buyerDtoToBuyer(buyerUpdateDto);
        Buyer response = buyerService.updateBuyer(userId, updateRequest);
        return buyerMapper.buyerToBuyerResponseDto(response);
    }

    @PostMapping("/favorites/{productId}")
    @ResponseStatus(value = HttpStatus.CREATED)
    public FavoriteResponseDto createFavorite(@RequestHeader long userId, @PathVariable Long productId) {
        Favorite response = favoriteService.create(userId, productId);
        return favoriteMapper.toFavouriteDto(response);
    }

    @DeleteMapping("/favorites/{productId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteFavorite(@RequestHeader long userId,
                               @PathVariable Long productId) {
        favoriteService.delete(userId, productId);
    }

    @GetMapping("/favorites")
    public FavouritesListResponseDto getBuyerFavouriteProducts(@RequestHeader long userId) {
        List<Favorite> favoriteList = favoriteService.getAll(userId);
        List<FavoriteResponseDto> response = favoriteList.stream()
                .map(favoriteMapper::toFavouriteDto)
                .collect(Collectors.toList());
        return favoriteMapper.toFavouritesListResponseDto(response);
    }
}
