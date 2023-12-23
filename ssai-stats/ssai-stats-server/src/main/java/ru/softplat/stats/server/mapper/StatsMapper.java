package ru.softplat.stats.server.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;
import ru.softplat.stats.dto.create.StatsCreateDto;
import ru.softplat.stats.server.dto.ReportEntry;
import ru.softplat.stats.server.dto.StatsResponseDto;
import ru.softplat.stats.server.model.SellerReport;
import ru.softplat.stats.server.model.Stats;

import java.util.List;

@Mapper(uses = ReportEntryMapper.class)
@Component
public interface StatsMapper {

    @Mapping(target = "reportEntryDtoList", source = "reportEntryList")
    @Mapping(target = "profit", source = "receiveAmount")
    StatsResponseDto sellerReportToStatsResponseDto(SellerReport sellerReport);

    Stats statsCreateDtoToStats(StatsCreateDto statsCreateDto);

    @Mapping(target = "sumRevenue", expression = "java(getSumRevenue(reportEntryList))")
    @Mapping(target = "receiveAmount", expression = "java(getReceiveAmount(reportEntryList))")
    default SellerReport listEntriesToSellerReport(List<ReportEntry> reportEntryList) {
        return new SellerReport(reportEntryList,
                getSumRevenue(reportEntryList),
                getReceiveAmount(reportEntryList));
    }

    default double getSumRevenue(List<ReportEntry> reportEntries) {
        return reportEntries.stream()
                .map(ReportEntry::getCommonProfit)
                .reduce(0D, Double::sum);
    }

    default double getReceiveAmount(List<ReportEntry> reportEntries) {
        return reportEntries.stream()
                .map(ReportEntry::getProfit)
                .reduce(0D, Double::sum);
    }
}
