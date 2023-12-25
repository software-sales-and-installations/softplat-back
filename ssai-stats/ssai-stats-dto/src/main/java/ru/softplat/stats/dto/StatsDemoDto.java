package ru.softplat.stats.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.softplat.stats.dto.create.StatBuyerDto;
import ru.softplat.stats.dto.create.StatProductDto;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StatsDemoDto {
    StatBuyerDto buyer;
    StatProductDto product;
    Long demoQuantity;
}