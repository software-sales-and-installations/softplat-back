package ru.yandex.workshop.stats.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.yandex.workshop.stats.model.SellerReportEntry;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class StatsResponseDto {

    List<SellerReportEntry> sellerReportEntryList;
    Float sumRevenue;
}
