package ru.yandex.workshop.main.dto.basket;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.yandex.workshop.main.dto.buyer.BuyerMapper;
import ru.yandex.workshop.main.dto.buyer.BuyerResponseDto;
import ru.yandex.workshop.main.model.buyer.Basket;
import ru.yandex.workshop.main.model.buyer.Buyer;
import ru.yandex.workshop.main.model.buyer.ProductBasket;

@Mapper
public interface BasketMapper {

    BasketMapper INSTANCE = Mappers.getMapper(BasketMapper.class);

    BasketDto basketToBasketDto(Basket basket);

    default BuyerResponseDto buyerToBuyerResponseDto(Buyer buyer) {
        return BuyerMapper.INSTANCE.buyerToBuyerResponseDto(buyer);
    }

    default ProductBasketDto productBasketToDto(ProductBasket productBasket) {
        //TODO product mapper
        if (productBasket == null) return null;
        return new ProductBasketDto(null, productBasket.getQuantity());
    }
}