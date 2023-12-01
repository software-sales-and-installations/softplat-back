package ru.softplat.stats.server.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.softplat.stats.dto.SortEnum;
import ru.softplat.stats.dto.StatsCreateDto;
import ru.softplat.stats.dto.StatsFilterAdmin;
import ru.softplat.stats.dto.StatsFilterSeller;
import ru.softplat.stats.server.dto.StatsResponseDto;
import ru.softplat.stats.server.mapper.StatsMapper;
import ru.softplat.stats.server.service.StatsService;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/stats")
@Slf4j
@Validated
public class StatsController {

    private final StatsService statsService;
    private final StatsMapper statsMapper;

    @GetMapping(path = "/admin/seller")
    public StatsResponseDto getSellerReportAdmin(@RequestBody StatsFilterAdmin statsFilterAdmin,
            @RequestParam(name = "sort", defaultValue = "POPULAR")  SortEnum sort) {
        return statsMapper.sellerReportToStatsResponseDto(
                statsService
                        .getSellerReportAdmin(
                                statsFilterAdmin,
                                sort));
    }

    @GetMapping(path = "/admin/product")
    public StatsResponseDto getProductReportAdmin(@RequestBody StatsFilterSeller statsFilterSeller,
            @RequestParam(name = "sort", defaultValue = "POPULAR") SortEnum sort) {
        return statsMapper.sellerReportToStatsResponseDto(
                statsService
                        .getProductReportAdmin(
                                statsFilterSeller,
                                sort));
    }

    @GetMapping(path = "/seller")
    public StatsResponseDto getProductsReportSeller(
            @RequestHeader Long sellerId,
            @RequestBody StatsFilterSeller statsFilterSeller,
            @RequestParam(name = "sort", defaultValue = "POPULAR") SortEnum sort) {
        return statsMapper.sellerReportToStatsResponseDto(
                statsService
                        .getProductsReportSeller(
                                sellerId,
                                statsFilterSeller,
                                sort));
    }

    @PostMapping
    public void createStat(@RequestBody @Valid StatsCreateDto statsCreateDto) {
        log.debug("Попытка создания статистики");
        statsService.createStats(statsMapper.statsCreateDtoToStats(statsCreateDto));
    }
}