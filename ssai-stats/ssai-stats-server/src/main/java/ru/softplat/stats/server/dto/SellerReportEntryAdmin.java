package ru.softplat.stats.server.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SellerReportEntryAdmin {

    LocalDateTime date;

    String productName;

    Long articleNumber;

    String sellerName;

    Long quantity;

    Double revenue;

    Double receiveAmountAdmin;
}
