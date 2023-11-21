package ru.yandex.workshop.main.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;
import ru.yandex.workshop.main.dto.user.response.FavoriteResponseDto;
import ru.yandex.workshop.main.model.buyer.Favorite;

@Mapper
@Component
public interface FavoriteMapper {
    @Mapping(target = "userId", source = "model.buyer.id")
    FavoriteResponseDto toFavouriteDto(Favorite model);
}
