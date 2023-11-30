package ru.softplat.main.server.mapper;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;
import ru.softplat.main.dto.basket.BasketResponseDto;
import ru.softplat.main.server.model.buyer.Basket;


@Mapper(uses = BasketPositionMapper.class)
@Component
public interface BasketMapper {
    BasketResponseDto basketToBasketResponseDto(Basket basket);
}