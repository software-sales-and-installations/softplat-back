package ru.yandex.workshop.stats.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class StatsResponseDto {

    Long id;

    String product;

    LocalDate purchaseDate;

    Long quantity;

    Long amount;
}
