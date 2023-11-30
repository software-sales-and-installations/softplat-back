package ru.softplat.stats.server.mapper;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;
import ru.softplat.stats.server.dto.StatsResponseDto;
import ru.softplat.stats.server.model.SellerReport;
import ru.softplat.stats.server.model.Stats;
import ru.softplat.stats.dto.StatsCreateDto;

@Mapper(componentModel = "spring", uses = StatsMapper.class)
@Component
public interface StatsMapper {

    StatsResponseDto sellerReportToStatsResponseDto(SellerReport sellerReport);

    Stats statsCreateDtoToStats(StatsCreateDto statsCreateDto);
}
