package ru.yandex.workshop.main.mapper;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;
import ru.yandex.workshop.main.dto.basket.BasketResponseDto;
import ru.yandex.workshop.main.model.buyer.Basket;

@Mapper(componentModel = "spring", uses = BasketPositionMapper.class)
@Component
public interface BasketMapper {
    BasketResponseDto basketToBasketDto(Basket basket);
}