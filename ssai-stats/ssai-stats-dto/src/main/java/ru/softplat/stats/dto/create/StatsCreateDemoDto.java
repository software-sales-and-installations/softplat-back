package ru.softplat.stats.dto.create;

import lombok.*;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StatsCreateDemoDto {
    StatBuyerDto buyer;
    StatProductDto product;
}