package ru.softplat.stats.server.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.softplat.stats.server.dto.ReportEntryDto;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Report {
    List<ReportEntryDto> reportEntryList;
    Double sumRevenue;
    Double receiveAmount;
}
