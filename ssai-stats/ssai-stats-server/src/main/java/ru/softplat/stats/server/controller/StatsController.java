package ru.softplat.stats.server.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.softplat.stats.dto.StatsFilterAdmin;
import ru.softplat.stats.dto.StatsFilterSeller;
import ru.softplat.stats.server.dto.StatsResponseDto;
import ru.softplat.stats.server.mapper.StatsMapper;
import ru.softplat.stats.server.message.LogMessage;
import ru.softplat.stats.dto.SortEnum;
import ru.softplat.stats.server.service.StatsService;
import ru.softplat.stats.dto.StatsCreateDto;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/stats")
@Slf4j
@Validated
@Tag(name = "StatsController", description = "Контроллер предназначен для работы со статистикой")
public class StatsController {

    private final StatsService statsService;
    private final StatsMapper statsMapper;

    @Operation(summary = "Получение статистики по продажам продавцов админом", description = "Доступ для админа")
    @PreAuthorize("hasAuthority('admin:write')")
    @GetMapping(path = "/admin/seller")
    public StatsResponseDto getSellerReportAdmin(
            @Parameter(description = "Фильтрация для получения статистики: " +
                    "по категории/продавцу/производителю/дате") StatsFilterAdmin statsFilterAdmin,
            @RequestParam(name = "sort", defaultValue = "POPULAR")
            @Parameter(description = "Условие сортировки для статистики: " +
                    "по количеству продаж/по стоимости") SortEnum sort) {
        log.debug(LogMessage.TRY_GET_STATS_SELLER_ADMIN.label);
        return statsMapper.sellerReportToStatsResponseDto(
                statsService
                        .getSellerReportAdmin(
                                statsFilterAdmin,
                                sort));
    }

    @Operation(summary = "Получение статистики по продажам продуктов админом", description = "Доступ для админа")
    @PreAuthorize("hasAuthority('admin:write')")
    @GetMapping(path = "/admin/product")
    public StatsResponseDto getProductReportAdmin(
            @Parameter(description = "Фильтрация для получения статистики: " +
                    "по категории/производителю/дате") StatsFilterSeller statsFilterSeller,
            @RequestParam(name = "sort", defaultValue = "POPULAR")
            @Parameter(description = "Условие сортировки для статистики: " +
                    "по количеству продаж/по стоимости") SortEnum sort) {
        log.debug(LogMessage.TRY_GET_STATS_PRODUCT_ADMIN.label);
        return statsMapper.sellerReportToStatsResponseDto(
                statsService
                        .getProductReportAdmin(
                                statsFilterSeller,
                                sort));
    }

    @Operation(summary = "Получение статистики по продажам продуктов продавца", description = "Доступ для продавца")
    @PreAuthorize("hasAuthority('seller:write')")
    @GetMapping(path = "/seller")
    public StatsResponseDto getProductsReportSeller(
            @RequestHeader Long sellerId,
            @Parameter(description = "Фильтрация для получения статистики: " +
                    "по категории/производителю/дате") StatsFilterSeller statsFilterSeller,
            @RequestParam(name = "sort", defaultValue = "POPULAR")
            @Parameter(description = "Условие сортировки для статистики: " +
                    "по количеству продаж/по стоимости") SortEnum sort) {
        log.debug(LogMessage.TRY_GET_STATS_PRODUCT_SELLER.label);
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