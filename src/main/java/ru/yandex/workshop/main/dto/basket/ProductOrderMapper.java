package ru.yandex.workshop.main.dto.basket;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.yandex.workshop.main.model.buyer.ProductBasket;
import ru.yandex.workshop.main.model.buyer.ProductOrder;

@Mapper
public interface ProductOrderMapper {

    ProductOrderMapper INSTANCE = Mappers.getMapper(ProductOrderMapper.class);

    ProductOrder productBasketToProductOrder(ProductBasket productBasket);

    ProductOrderDto productOrderToDto(ProductOrder productOrder);
}
