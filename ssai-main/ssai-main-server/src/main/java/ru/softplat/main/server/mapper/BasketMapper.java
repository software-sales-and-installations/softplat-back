package ru.softplat.main.server.mapper;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;
import ru.softplat.main.server.model.buyer.Basket;
import ru.softplat.dto.basket.BasketResponseDto;


@Mapper(uses = BasketPositionMapper.class)
@Component
public interface BasketMapper {
    BasketResponseDto basketToBasketResponseDto(Basket basket);
}