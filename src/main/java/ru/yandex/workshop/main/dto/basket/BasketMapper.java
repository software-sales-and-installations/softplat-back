package ru.yandex.workshop.main.dto.basket;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.yandex.workshop.main.dto.product.ProductMapper;
import ru.yandex.workshop.main.model.buyer.Basket;
import ru.yandex.workshop.main.model.buyer.ProductBasket;

@Mapper
public interface BasketMapper {

    BasketMapper INSTANCE = Mappers.getMapper(BasketMapper.class);

    BasketDto basketToBasketDto(Basket basket);

    default ProductBasketDto productBasketToDto(ProductBasket productBasket) {
        return new ProductBasketDto(ProductMapper.INSTANCE.productToProductResponseDto(productBasket.getProduct()),
                productBasket.getQuantity(), productBasket.getInstallation());
    }
}