package ru.softplat.stats.server.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class StatsResponseDto {

    List<SellerReportEntryAdmin> sellerReportEntryList;
    Float sumRevenue;
    Float receiveAmount;
}
