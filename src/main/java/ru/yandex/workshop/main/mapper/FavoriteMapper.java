package ru.yandex.workshop.main.mapper;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;
import ru.yandex.workshop.main.dto.user.response.FavoriteDto;
import ru.yandex.workshop.main.model.buyer.Favorite;

@Mapper(componentModel = "spring")
@Component
public interface FavoriteMapper {

    FavoriteDto toFavouriteDto(Favorite model);
}
