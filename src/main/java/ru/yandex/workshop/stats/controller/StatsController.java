package ru.yandex.workshop.stats.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.workshop.main.message.LogMessage;
import ru.yandex.workshop.stats.dto.StatsFilterAdmin;
import ru.yandex.workshop.stats.dto.StatsFilterSeller;
import ru.yandex.workshop.stats.dto.StatsResponseDto;
import ru.yandex.workshop.stats.mapper.StatsMapper;
import ru.yandex.workshop.stats.model.SortEnum;
import ru.yandex.workshop.stats.service.StatsService;
import springfox.documentation.annotations.ApiIgnore;

import java.security.Principal;

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
            @ApiIgnore Principal principal,
            @Parameter(description = "Фильтрация для получения статистики: " +
                    "по категории/производителю/дате") StatsFilterSeller statsFilterSeller,
            @RequestParam(name = "sort", defaultValue = "POPULAR")
            @Parameter(description = "Условие сортировки для статистики: " +
                    "по количеству продаж/по стоимости") SortEnum sort) {
        log.debug(LogMessage.TRY_GET_STATS_PRODUCT_SELLER.label);
        return statsMapper.sellerReportToStatsResponseDto(
                statsService
                        .getProductsReportSeller(
                                principal.getName(),
                                statsFilterSeller,
                                sort));
    }
}