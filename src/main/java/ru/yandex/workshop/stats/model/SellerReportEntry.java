package ru.yandex.workshop.stats.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SellerReportEntry {

    String productName;

    Integer quantity;

    Float revenue;
}
