package ru.softplat.main.dto.basket;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BasketCreateDto {
    List<BasketPositionCreateDto> productsInBasket;
}
