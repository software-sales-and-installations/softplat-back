package ru.softplat.stats.server.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.softplat.stats.server.dto.SellerReportEntry;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SellerReport {

    List<SellerReportEntry> sellerReportEntryList;
    Double sumRevenue;
}
