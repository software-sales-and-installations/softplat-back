package ru.softplat.stats.server.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.softplat.stats.dto.SortEnum;
import ru.softplat.stats.dto.StatsFilter;
import ru.softplat.stats.dto.StatsFilterSeller;
import ru.softplat.stats.dto.create.StatsCreateDto;
import ru.softplat.stats.server.dto.StatsResponseDto;
import ru.softplat.stats.server.mapper.StatsMapper;
import ru.softplat.stats.server.service.StatsService;

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
            @RequestBody(required = false) StatsFilter statsFilter,
            @RequestParam(name = "sort", defaultValue = "POPULAR") SortEnum sort) {
        return statsMapper.sellerReportToStatsResponseDto(
                statsService.getSellerReportAdmin(statsFilter, sort));
    }

    @PostMapping(path = "/seller")
    public StatsResponseDto getProductReportSeller(
            @RequestHeader("X-Sharer-User-Id") Long sellerId,
            @RequestBody(required = false) StatsFilterSeller statsFilterSeller,
            @RequestParam(name = "sort", defaultValue = "POPULAR") SortEnum sort) {
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

    @GetMapping(path = "/admin/file")
    public boolean saveAdminFile() {
        return statsService.saveAdminFile();
    }

    @GetMapping(path = "/seller/file")
    public boolean saveSellerFile() {
        return statsService.saveSellerFile();
    }
}