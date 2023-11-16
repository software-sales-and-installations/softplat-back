package ru.yandex.workshop.main.dto.basket;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BasketResponseDto {
    private Long id;
    private Long buyerId;
    private List<BasketPositionResponseDto> productsInBasket;
}
