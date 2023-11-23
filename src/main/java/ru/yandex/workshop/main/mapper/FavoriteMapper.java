package ru.yandex.workshop.main.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;
import ru.yandex.workshop.main.dto.user.response.FavoriteResponseDto;
import ru.yandex.workshop.main.dto.user.response.FavouritesListResponseDto;
import ru.yandex.workshop.main.model.buyer.Favorite;

import java.util.List;

@Mapper
@Component
public interface FavoriteMapper {
    @Mapping(target = "userId", source = "model.buyer.id")
    FavoriteResponseDto toFavouriteDto(Favorite model);

    default FavouritesListResponseDto toFavouritesListResponseDto(List<FavoriteResponseDto> favorites) {
        return FavouritesListResponseDto.builder().favorites(favorites).build();
    }
}
