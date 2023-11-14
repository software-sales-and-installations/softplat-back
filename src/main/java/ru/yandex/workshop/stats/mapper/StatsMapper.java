package ru.yandex.workshop.stats.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;
import ru.yandex.workshop.stats.dto.StatsResponseDto;
import ru.yandex.workshop.stats.model.SellerReport;

@Mapper
@Component
public interface StatsMapper {

    StatsMapper INSTANCE = Mappers.getMapper(StatsMapper.class);

    StatsResponseDto sellerReportToStatsResponseDto(SellerReport sellerReport);

}
