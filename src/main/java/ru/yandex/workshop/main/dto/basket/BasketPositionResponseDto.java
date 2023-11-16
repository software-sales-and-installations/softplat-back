package ru.yandex.workshop.main.dto.basket;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.yandex.workshop.main.dto.product.ProductResponseDto;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BasketPositionResponseDto {
    private Long id;
    private ProductResponseDto productResponseDto;
    private Integer quantity;
    private Boolean installation;
}
