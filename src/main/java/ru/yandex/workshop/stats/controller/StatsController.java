package ru.yandex.workshop.stats.controller;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.workshop.stats.dto.StatsFilterAdmin;
import ru.yandex.workshop.stats.dto.StatsFilterSeller;
import ru.yandex.workshop.stats.model.SellerReport;
import ru.yandex.workshop.stats.service.StatsService;

import javax.validation.constraints.Min;
import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping( "/product")
@Slf4j
@Validated
public class StatsController {

    private final StatsService statsService;

    //@PreAuthorize("hasAuthority('admin:write')")
    @GetMapping(path = "/stats/admin/seller")
    public SellerReport getSellerReportAdmin(
            @RequestBody StatsFilterAdmin statsFilterAdmin,
            @RequestParam(name = "start") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate start,
            @RequestParam(name = "end") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end,
            @RequestParam(name = "sort", defaultValue = "quantity") String sort,
            @RequestParam(name = "from", defaultValue = "0") @Min(0) int from,
            @RequestParam(name = "size", defaultValue = "20") @Min(1) int size) {
        return statsService.getSellerReportAdmin(statsFilterAdmin, start, end, sort, from, size);
    }

    @PreAuthorize("hasAuthority('admin:write')")
    @GetMapping(path = "/stats/admin/product")
    public List<SellerReport> getProductReportAdmin(
            @RequestBody StatsFilterAdmin statsFilterAdmin,
            @RequestParam(name = "start") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate start,
            @RequestParam(name = "end") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end,
            @RequestParam(name = "sort", defaultValue = "popular") String sort,
            @RequestParam(name = "from", defaultValue = "0") @Min(0) int from,
            @RequestParam(name = "size", defaultValue = "20") @Min(1) int size) {
        return statsService.getProductReportAdmin(statsFilterAdmin, start, end, sort, from, size);
    }

    @PreAuthorize("hasAuthority('seller:write')")
    @GetMapping(path = "/stats/seller")
    public List<SellerReport> getProductsReportSeller(
            Principal principal,
            @RequestBody StatsFilterSeller statsFilterSeller,
            @RequestParam(name = "start") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate start,
            @RequestParam(name = "end") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end,
            @RequestParam(name = "sort", defaultValue = "popular") String sort,
            @RequestParam(name = "from", defaultValue = "0") @Min(0) int from,
            @RequestParam(name = "size", defaultValue = "20") @Min(1) int size) {
        return statsService.getProductsReportSeller(principal.getName(), statsFilterSeller, start, end, sort, from, size);
    }
}