package ru.yandex.workshop.main.dto.user.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;
import ru.yandex.workshop.main.dto.user.response.FavoriteDto;
import ru.yandex.workshop.main.model.buyer.Buyer;
import ru.yandex.workshop.main.model.buyer.Favorite;
import ru.yandex.workshop.main.model.product.Product;

@Mapper(componentModel = "spring")
@Component
public interface FavoriteMapper {

    @Mapping(target = "id", source = "id")
    Favorite toModel(Long id, Buyer buyer, Product product);

    FavoriteDto toDTO(Favorite model);
}
