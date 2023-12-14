package ru.softplat.stats.server.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.softplat.stats.server.dto.SellerReportEntryAdmin;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SellerReport {

    List<SellerReportEntryAdmin> sellerReportEntryList;
    Double sumRevenue;
    Double receiveAmount;
}
