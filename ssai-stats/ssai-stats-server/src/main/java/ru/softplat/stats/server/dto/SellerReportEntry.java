package ru.softplat.stats.server.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SellerReportEntry {

    String productName;

    Long quantity;

    Double revenue;

    public SellerReportEntry(String productName, Long quantity, Double revenue) {
        this.productName = productName;
        this.quantity = quantity;
        this.revenue = revenue;
    }
}
