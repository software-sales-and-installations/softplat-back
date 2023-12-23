package ru.softplat.stats.server.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.softplat.stats.server.dto.ReportEntry;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SellerReport {

    List<ReportEntry> reportEntryList;
    Double sumRevenue;
    Double receiveAmount;
}
