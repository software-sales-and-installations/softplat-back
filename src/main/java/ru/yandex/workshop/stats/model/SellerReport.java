package ru.yandex.workshop.stats.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SellerReport {

    List<SellerReportEntry> sellerReportEntryList;
    Float sumRevenue;
}
