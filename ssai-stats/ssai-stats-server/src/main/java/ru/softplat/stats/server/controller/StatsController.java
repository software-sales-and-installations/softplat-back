package ru.softplat.stats.server.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.softplat.stats.dto.SortEnum;
import ru.softplat.stats.dto.StatsFilterAdmin;
import ru.softplat.stats.dto.StatsFilterSeller;
import ru.softplat.stats.dto.create.StatsCreateDto;
import ru.softplat.stats.server.dto.StatsResponseDto;
import ru.softplat.stats.server.mapper.StatsMapper;
import ru.softplat.stats.server.service.StatsService;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/stats")
@Slf4j
public class StatsController {

    private final StatsService statsService;
    private final StatsMapper statsMapper;

    @PostMapping(path = "/admin")
    public StatsResponseDto getSellerReportAdmin(
            @RequestBody(required = false) StatsFilterAdmin statsFilterAdmin,
            @RequestParam(name = "sort", defaultValue = "POPULAR") SortEnum sort) throws IOException {
        return statsMapper.sellerReportToStatsResponseDto(
                statsService
                        .getSellerReportAdmin(

                                statsFilterAdmin,
                                sort));
    }

//    @GetMapping(path = "/admin")
//    public StatsResponseDto getProductReportAdmin(/*@RequestBody*/ StatsFilterSeller statsFilterSeller,
//            @RequestParam(name = "sort", defaultValue = "POPULAR") SortEnum sort) throws IOException {
//        return statsMapper.sellerReportToStatsResponseDto(
//                statsService
//                        .getProductReportAdmin(
//                                statsFilterSeller,
//                                sort));
//    }

    @PostMapping(path = "/seller")
    public StatsResponseDto getProductReportSeller(
            @RequestHeader("X-Sharer-User-Id") Long sellerId,
            @RequestBody(required = false) StatsFilterSeller statsFilterSeller,
            @RequestParam(name = "sort", defaultValue = "POPULAR") SortEnum sort) throws IOException {
        return statsMapper.sellerReportToStatsResponseDto(
                statsService
                        .getProductReportSeller(
                                sellerId,
                                statsFilterSeller,
                                sort));
    }

    @PostMapping(path = "/demo")
    public void downloadDemo(@RequestHeader("X-Sharer-User-Id") Long productId) {
        statsService.downloadDemo(productId);
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