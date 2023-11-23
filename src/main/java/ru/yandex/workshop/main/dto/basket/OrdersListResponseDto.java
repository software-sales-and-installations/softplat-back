package ru.yandex.workshop.main.dto.basket;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrdersListResponseDto {
    List<OrderResponseDto> orders;
}
