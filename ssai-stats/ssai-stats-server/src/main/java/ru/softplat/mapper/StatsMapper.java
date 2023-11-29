package ru.softplat.mapper;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;
import ru.softplat.dto.StatsResponseDto;
import ru.softplat.model.SellerReport;
import ru.softplat.model.Stats;
import ru.softplat.stats.dto.StatsCreateDto;

@Mapper(componentModel = "spring", uses = StatsMapper.class)
@Component
public interface StatsMapper {

    StatsResponseDto sellerReportToStatsResponseDto(SellerReport sellerReport);

    Stats statsCreateDtoToStats(StatsCreateDto statsCreateDto);
}
