package ru.softplat.stats.server.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReportEntry {

    String productName;

    Long articleNumber;

    String sellerName;

    Long quantity;

    Double commonProfit;

    Double profit;
}
