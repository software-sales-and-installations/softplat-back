package ru.yandex.workshop.main.dto.basket;

import lombok.*;
import ru.yandex.workshop.main.dto.product.ProductResponseDto;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OrderPositionResponseDto {
    private Long id;
    private ProductResponseDto productResponseDto;
    private Integer quantity;
    private Boolean installation;
    private Float productCost;
}
