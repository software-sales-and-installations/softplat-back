package ru.yandex.workshop.main.dto.basket;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BasketDto {
    private Long id;
    private Long buyerId;
    private List<BasketPositionDto> productsInBasket;
}
