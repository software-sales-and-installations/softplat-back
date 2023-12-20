package ru.softplat.main.server.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;
import ru.softplat.main.dto.basket.BasketPositionCreateDto;
import ru.softplat.main.dto.basket.BasketPositionResponseDto;
import ru.softplat.main.server.model.buyer.BasketPosition;
import ru.softplat.main.server.model.product.Product;


@Mapper(uses = ProductMapper.class)
@Component
public interface BasketPositionMapper {
    @Mapping(target = "productResponseDto", source = "product")
    BasketPositionResponseDto basketPositionToDto(BasketPosition basketPosition);

    default BasketPosition createBasketPosition(BasketPositionCreateDto basketPositionCreateDto, Product product) {
        return BasketPosition.builder()
                .installation(basketPositionCreateDto.getInstallation())
                .quantity(basketPositionCreateDto.getQuantity())
                .product(product)
                .build();
    }
}