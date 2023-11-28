package ru.softplat.mapper;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;
import ru.softplat.dto.basket.BasketResponseDto;
import ru.softplat.model.buyer.Basket;


@Mapper(uses = BasketPositionMapper.class)
@Component
public interface BasketMapper {
    BasketResponseDto basketToBasketResponseDto(Basket basket);
}