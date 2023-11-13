package ru.yandex.workshop.main.mapper;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;
import ru.yandex.workshop.main.dto.basket.BasketDto;
import ru.yandex.workshop.main.dto.basket.ProductBasketDto;
import ru.yandex.workshop.main.model.buyer.Basket;
import ru.yandex.workshop.main.model.buyer.ProductBasket;

@Mapper(componentModel = "spring", uses = ProductMapper.class)
@Component
public interface BasketMapper {

    BasketDto basketToBasketDto(Basket basket);

    default ProductBasketDto productBasketToDto(ProductBasket productBasket, ProductMapper productMapper) {
        return new ProductBasketDto(productMapper.productToProductResponseDto(productBasket.getProduct()),
                productBasket.getQuantity(), productBasket.getInstallation());
    }
}