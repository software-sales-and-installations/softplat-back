package ru.softplat.stats.server.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;
import ru.softplat.stats.server.dto.ReportEntryDto;
import ru.softplat.stats.server.model.ReportEntry;

@Mapper
@Component
public interface ReportEntryMapper {

    @Mapping(target = "profit", source = "profitAdmin")
    @Mapping(target = "demo", ignore = true)
    ReportEntryDto reportEntryToAdminDto(ReportEntry reportEntry);

    @Mapping(target = "profit", source = "reportEntry.profitSeller")
    ReportEntryDto reportEntryToSellerDto(ReportEntry reportEntry, int demo);
}
