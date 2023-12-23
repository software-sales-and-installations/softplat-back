package ru.softplat.main.dto.basket;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.softplat.main.dto.product.ProductResponseDto;

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
