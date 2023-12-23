package ru.softplat.security.server.web.controller.stats;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.softplat.security.server.message.LogMessage;
import ru.softplat.stats.client.StatsClient;
import ru.softplat.stats.dto.SortEnum;
import ru.softplat.stats.dto.StatsFilterAdmin;
import ru.softplat.stats.dto.StatsFilterSeller;

import javax.validation.Valid;


@RestController
@RequiredArgsConstructor
@RequestMapping("/stats")
@Slf4j
@Validated
public class StatsController {

    private final StatsClient statsClient;

    @Operation(summary = "Получение статистики по продажам продавцов админом", description = "Доступ для админа")
    @PreAuthorize("hasAuthority('admin:write')")
    @GetMapping(path = "/admin")
    public ResponseEntity<Object> getSellerReportAdmin(
            @Parameter(description = "Фильтрация для получения статистики: по категории/продавцу/производителю/дате")
            @RequestBody @Valid StatsFilterAdmin statsFilterAdmin,
            @RequestParam(name = "sort", defaultValue = "POPULAR")
            @Parameter(description = "Условие сортировки для статистики: " +
                    "по количеству продаж/по стоимости") SortEnum sort) {
        log.debug(LogMessage.TRY_GET_STATS_SELLER_ADMIN.label);
        return statsClient.getSellerReportAdmin(statsFilterAdmin, sort);
    }

    @Operation(summary = "Получение статистики по продажам продуктов продавца", description = "Доступ для продавца")
    @PreAuthorize("hasAuthority('seller:write')")
    @GetMapping(path = "/seller")
    public ResponseEntity<Object> getProductReportSeller(
            @RequestHeader("X-Sharer-User-Id") Long sellerId,
            @Parameter(description = "Фильтрация для получения статистики: по категории/производителю/дате")
            @RequestBody @Valid StatsFilterSeller statsFilterSeller,
            @RequestParam(name = "sort", defaultValue = "POPULAR")
            @Parameter(description = "Условие сортировки для статистики: " +
                    "по количеству продаж/по стоимости") SortEnum sort) {
        log.debug(LogMessage.TRY_GET_STATS_PRODUCT_SELLER.label);
        return statsClient.getProductReportSeller(sellerId, statsFilterSeller, sort);
    }
}
