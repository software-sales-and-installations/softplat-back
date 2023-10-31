package ru.yandex.workshop.main.dto.basket;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.yandex.workshop.main.dto.buyer.BuyerResponseDto;
import ru.yandex.workshop.main.model.buyer.ProductBasket;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BasketDto {
    private BuyerResponseDto buyerResponseDto;
    private final List<ProductBasket> productsInBasket = new ArrayList<>();
}
