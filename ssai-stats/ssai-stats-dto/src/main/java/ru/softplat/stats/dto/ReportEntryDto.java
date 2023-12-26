package ru.softplat.stats.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReportEntryDto {

    String productName;

    String sellerName;

    Long articleNumber;

    Long quantity;

    Double commonProfit;

    Double profit;

    int demo;
}
