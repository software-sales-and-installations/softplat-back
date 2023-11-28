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
import ru.softplat.mapper.StatsMapper;
import ru.softplat.message.LogMessage;
import ru.softplat.model.SortEnum;
import ru.softplat.service.StatsService;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.List;

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

    @PostMapping
    @PreAuthorize("hasAuthority('buyer:write')")
    public void createStats(HttpServletRequest request) {
        List<OrderPosition> orderPositionList = order.getProductsOrdered();
        for (OrderPosition orderPosition : orderPositionList) {
            Stats stats = new Stats();
            stats.setProduct(orderPosition.getProduct());
            stats.setBuyer(order.getBuyer());
            stats.setDateBuy(order.getProductionTime());
            stats.setQuantity((long) orderPosition.getQuantity());
            stats.setAmount((double) COMMISSIONS * orderPosition.getProductCost());
            statsService.createStats(stats);
        }
    }
}