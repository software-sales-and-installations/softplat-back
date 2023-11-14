package ru.yandex.workshop.stats.controller;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.workshop.stats.dto.StatsFilterAdmin;
import ru.yandex.workshop.stats.dto.StatsFilterSeller;
import ru.yandex.workshop.stats.dto.StatsResponseDto;
import ru.yandex.workshop.stats.mapper.StatsMapper;
import ru.yandex.workshop.stats.service.StatsService;

import java.security.Principal;
import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/product/stats")
@Slf4j
@Validated
public class StatsController {

    private final StatsService statsService;

    //@PreAuthorize("hasAuthority('admin:write')")
    @GetMapping(path = "/admin/seller")
    public StatsResponseDto getSellerReportAdmin(
            @RequestBody StatsFilterAdmin statsFilterAdmin/*,
            @RequestParam(name = "sort", defaultValue = "quantity") String sort*/) {
        return StatsMapper.INSTANCE.sellerReportToStatsResponseDto(
                statsService
                        .getSellerReportAdmin(
                                statsFilterAdmin
                                /*,
                                sort*/));
    }

    @PreAuthorize("hasAuthority('admin:write')")
    @GetMapping(path = "/admin/product")
    public StatsResponseDto getProductReportAdmin(
            @RequestBody StatsFilterAdmin statsFilterAdmin,
            @RequestParam(name = "start") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate start,
            @RequestParam(name = "end") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end,
            @RequestParam(name = "sort", defaultValue = "popular") String sort) {
        return StatsMapper.INSTANCE.sellerReportToStatsResponseDto(
                statsService
                        .getProductReportAdmin(
                                statsFilterAdmin,
                                start,
                                end,
                                sort));
    }

    @PreAuthorize("hasAuthority('seller:write')")
    @GetMapping(path = "/seller")
    public StatsResponseDto getProductsReportSeller(
            Principal principal,
            @RequestBody StatsFilterSeller statsFilterSeller,
            @RequestParam(name = "start") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate start,
            @RequestParam(name = "end") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end,
            @RequestParam(name = "sort", defaultValue = "popular") String sort) {
        return StatsMapper.INSTANCE.sellerReportToStatsResponseDto(
                statsService
                        .getProductsReportSeller(
                                principal.getName(),
                                statsFilterSeller,
                                start,
                                end,
                                sort));
    }

    //@PreAuthorize("hasAuthority('seller:write')")
    @PostMapping
    public void createStats() {
        statsService.createStats();
    }
}