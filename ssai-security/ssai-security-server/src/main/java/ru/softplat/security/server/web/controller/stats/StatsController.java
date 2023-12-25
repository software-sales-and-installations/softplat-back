package ru.softplat.security.server.web.controller.stats;

import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
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
import ru.softplat.stats.dto.StatsFilter;
import ru.softplat.stats.dto.StatsFilterSeller;
import ru.softplat.stats.dto.StatsResponseDto;

import javax.validation.Valid;


@RestController
@RequiredArgsConstructor
@RequestMapping("/stats")
@Slf4j
@Validated
public class StatsController {

    private final StatsClient statsClient;

    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK", response = StatsResponseDto.class)})
    @Operation(summary = "Получение статистики по продажам продавцов админом", description = "Доступ для админа")
    @PreAuthorize("hasAuthority('admin:write')")
    @GetMapping(path = "/admin", produces = "application/json")
    public ResponseEntity<Object> getSellerReportAdmin(
            @Parameter(description = "Фильтрация для получения статистики: по продавцу/дате")
            @RequestBody @Valid StatsFilter statsFilter,
            @RequestParam(name = "sort", defaultValue = "POPULAR")
            @Parameter(description = "Условие сортировки для статистики: " +
                    "по количеству продаж/по стоимости") SortEnum sort) {
        log.debug(LogMessage.TRY_GET_STATS_SELLER_ADMIN.label);
        return statsClient.getSellerReportAdmin(statsFilter, sort);
    }

    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK", response = StatsResponseDto.class)})
    @Operation(summary = "Получение статистики по продажам продуктов продавца", description = "Доступ для продавца")
    @PreAuthorize("hasAuthority('seller:write')")
    @GetMapping(path = "/seller", produces = "application/json")
    public ResponseEntity<Object> getProductReportSeller(
            @RequestHeader("X-Sharer-User-Id") Long sellerId,
            @Parameter(description = "Фильтрация для получения статистики: по дате")
            @RequestBody @Valid StatsFilterSeller statsFilterSeller,
            @RequestParam(name = "sort", defaultValue = "POPULAR")
            @Parameter(description = "Условие сортировки для статистики: " +
                    "по количеству продаж/по стоимости") SortEnum sort) {
        log.debug(LogMessage.TRY_GET_STATS_PRODUCT_SELLER.label);
        return statsClient.getProductReportSeller(sellerId, statsFilterSeller, sort);
    }

    @Operation(summary = "Получение статистики по продажам продавцов админом - скачивание файла xls", description = "Доступ для админа")
    @PreAuthorize("hasAuthority('admin:write')")
    @GetMapping(path = "/admin/file")
    public ResponseEntity<Object> getSellerReportAdminFile(
            @Parameter(description = "Фильтрация для получения статистики: по продавцу/дате")
            @RequestBody @Valid StatsFilter statsFilter,
            @RequestParam(name = "sort", defaultValue = "POPULAR")
            @Parameter(description = "Условие сортировки для статистики: " +
                    "по количеству продаж/по стоимости") SortEnum sort) {
        log.debug(LogMessage.TRY_GET_STATS_SELLER_ADMIN_FILE.label);
        return statsClient.getFileReportAdmin(statsFilter, sort);
    }

    @Operation(summary = "Получение статистики по продажам продуктов продавца - скачивание файла xls", description = "Доступ для продавца")
    @PreAuthorize("hasAuthority('seller:write')")
    @GetMapping(path = "/seller/file")
    public ResponseEntity<Object> getProductReportSellerFile(
            @RequestHeader("X-Sharer-User-Id") Long sellerId,
            @Parameter(description = "Фильтрация для получения статистики: по дате")
            @RequestBody @Valid StatsFilterSeller statsFilterSeller,
            @RequestParam(name = "sort", defaultValue = "POPULAR")
            @Parameter(description = "Условие сортировки для статистики: " +
                    "по количеству продаж/по стоимости") SortEnum sort) {
        log.debug(LogMessage.TRY_GET_STATS_PRODUCT_SELLER_FILE.label);
        return statsClient.getFileReportSeller(sellerId, statsFilterSeller, sort);
    }
}
