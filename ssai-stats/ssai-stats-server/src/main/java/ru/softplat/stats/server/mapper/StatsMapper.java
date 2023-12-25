package ru.softplat.stats.server.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;
import ru.softplat.stats.dto.ReportEntryDto;
import ru.softplat.stats.dto.StatsResponseDto;
import ru.softplat.stats.dto.create.StatsCreateDto;
import ru.softplat.stats.server.model.Report;
import ru.softplat.stats.server.model.StatDemo;
import ru.softplat.stats.server.model.Stats;

import java.util.List;

@Mapper(uses = ReportEntryMapper.class)
@Component
public interface StatsMapper {

    @Mapping(target = "reportEntryDtoList", source = "reportEntryList")
    @Mapping(target = "profit", source = "receiveAmount")
    StatsResponseDto sellerReportToStatsResponseDto(Report report);

    Stats statsCreateDtoToStats(StatsCreateDto statsCreateDto);

    StatDemo statsDemoDtoToStatDemo(StatsCreateDto statsCreateDto);

    @Mapping(target = "sumRevenue", expression = "java(getSumRevenue(reportEntryList))")
    @Mapping(target = "receiveAmount", expression = "java(getReceiveAmount(reportEntryList))")
    default Report listEntriesToReport(List<ReportEntryDto> reportEntryList) {
        return new Report(reportEntryList,
                getSumRevenue(reportEntryList),
                getReceiveAmount(reportEntryList));
    }

    default double getSumRevenue(List<ReportEntryDto> reportEntries) {
        return reportEntries.stream()
                .map(ReportEntryDto::getCommonProfit)
                .reduce(0D, Double::sum);
    }

    default double getReceiveAmount(List<ReportEntryDto> reportEntries) {
        return reportEntries.stream()
                .map(ReportEntryDto::getProfit)
                .reduce(0D, Double::sum);
    }
}
