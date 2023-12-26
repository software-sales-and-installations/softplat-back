package ru.softplat.stats.server.model;

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

    Double profitAdmin;

    Double profitSeller;
}
