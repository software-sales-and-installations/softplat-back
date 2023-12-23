package ru.softplat.stats.server.mapper;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;
import ru.softplat.stats.server.dto.ReportEntry;
import ru.softplat.stats.server.dto.ReportEntryDto;

@Mapper
@Component
public interface ReportEntryMapper {

    ReportEntryDto reportEntryToDto(ReportEntry reportEntry);
}
