package ru.yandex.workshop.stats.mapper;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;
import ru.yandex.workshop.stats.dto.StatsResponseDto;
import ru.yandex.workshop.stats.model.SellerReport;

@Mapper(componentModel = "spring", uses = StatsMapper.class)
@Component
public interface StatsMapper {

    StatsResponseDto sellerReportToStatsResponseDto(SellerReport sellerReport);
}
