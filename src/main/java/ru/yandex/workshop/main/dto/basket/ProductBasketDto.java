package ru.yandex.workshop.main.dto.basket;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.yandex.workshop.main.dto.ProductDto;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProductBasketDto {
    private ProductDto productDto;
    private Integer quantity;
}
