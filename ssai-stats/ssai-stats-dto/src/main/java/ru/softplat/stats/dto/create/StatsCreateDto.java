package ru.softplat.stats.dto.create;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StatsCreateDto {
    StatBuyerDto buyer;
    StatProductDto product;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm", iso = DateTimeFormat.ISO.DATE_TIME)
    LocalDateTime dateBuy;
    int quantity;
    Double profit;
    Double profitSeller;
    Double profitAdmin;
}