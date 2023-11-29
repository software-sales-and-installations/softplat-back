package ru.softplat.mapper;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;
import ru.softplat.security.dto.StatsResponseDto;
import ru.softplat.stats.dto.StatsCreateDto;
import ru.softplat.model.SellerReport;
import ru.softplat.model.Stats;

@Mapper(componentModel = "spring", uses = StatsMapper.class)
@Component
public interface StatsMapper {

    StatsResponseDto sellerReportToStatsResponseDto(SellerReport sellerReport);
    Stats statsCreateDtoToStats(StatsCreateDto statsCreateDto);
}
