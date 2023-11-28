package ru.softplat.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.softplat.StatsFilterAdmin;
import ru.softplat.StatsFilterSeller;
import ru.softplat.StatsResponseDto;
import ru.softplat.create.StatsCreateDto;
import ru.softplat.mapper.StatsMapper;
import ru.softplat.message.LogMessage;
import ru.softplat.model.SortEnum;
import ru.softplat.service.StatsService;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/product/stats")
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
    @PreAuthorize("hasAuthority('buyer:write')")
    public void createStat(@RequestBody @Valid StatsCreateDto statsCreateDto) {
        log.debug("Попытка создания статистики");
        statsService.createStats(statsMapper.statsCreateDtoToStats(statsCreateDto));
    }
}