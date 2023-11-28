package ru.softplat.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;
import ru.softplat.dto.user.response.FavoriteResponseDto;
import ru.softplat.dto.user.response.FavouritesListResponseDto;
import ru.softplat.model.buyer.Favorite;

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
