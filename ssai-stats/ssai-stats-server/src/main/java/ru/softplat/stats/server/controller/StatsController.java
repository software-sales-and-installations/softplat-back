package ru.softplat.stats.server.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.softplat.stats.dto.SortEnum;
import ru.softplat.stats.dto.StatsFilter;
import ru.softplat.stats.dto.StatsFilterSeller;
import ru.softplat.stats.dto.StatsResponseDto;
import ru.softplat.stats.dto.create.StatsCreateDto;
import ru.softplat.stats.server.mapper.StatsMapper;
import ru.softplat.stats.server.service.StatsService;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/stats")
@Slf4j
public class StatsController {

    private final StatsService statsService;
    private final StatsMapper statsMapper;

    @PostMapping(path = "/admin")
    public StatsResponseDto getSellerReportAdmin(
            @RequestBody StatsFilter statsFilter,
            @RequestParam SortEnum sort) {
        return statsMapper.sellerReportToStatsResponseDto(
                statsService.getSellerReportAdmin(statsFilter, sort));
    }

    @PostMapping(path = "/seller")
    public StatsResponseDto getProductReportSeller(
            @RequestHeader("X-Sharer-User-Id") Long sellerId,
            @RequestBody StatsFilterSeller statsFilterSeller,
            @RequestParam SortEnum sort) {
        StatsFilter filter = StatsFilter.builder()
                .start(statsFilterSeller.getStart())
                .end(statsFilterSeller.getEnd())
                .sellerIds(List.of(sellerId))
                .build();
        return statsMapper.sellerReportToStatsResponseDto(
                statsService.getProductReportSeller(filter, sort));
    }

    @PostMapping(path = "/demo")
    public void downloadDemo(@RequestBody StatsCreateDto statsCreateDto) {
        statsService.downloadDemo(statsMapper.statsDemoDtoToStatDemo(statsCreateDto));
    }

    @PostMapping
    public void createStat(@RequestBody StatsCreateDto statsCreateDto) {
        log.debug("Попытка создания статистики");
        statsService.createStats(statsMapper.statsCreateDtoToStats(statsCreateDto));
    }

    @PostMapping(path = "/admin/file")
    public boolean saveAdminFile(
            @RequestBody StatsFilter statsFilter,
            @RequestParam SortEnum sort) throws IOException {
        return statsService.saveAdminFile(statsFilter, sort);
    }

    @PostMapping(path = "/seller/file")
    public boolean saveSellerFile(
            @RequestHeader("X-Sharer-User-Id") Long sellerId,
            @RequestBody StatsFilter statsFilter,
            @RequestParam SortEnum sort) throws IOException {
        StatsFilter filter = StatsFilter.builder()
                .start(statsFilter.getStart())
                .end(statsFilter.getEnd())
                .sellerIds(List.of(sellerId))
                .build();
        return statsService.saveSellerFile(filter, sort);
    }
}