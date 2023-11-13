package ru.yandex.workshop.stats.controller;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.workshop.main.model.buyer.Order;
import ru.yandex.workshop.stats.dto.StatsFilterAdmin;
import ru.yandex.workshop.stats.dto.StatsFilterSeller;
import ru.yandex.workshop.stats.dto.StatsResponseDto;
import ru.yandex.workshop.stats.model.SellerReport;
import ru.yandex.workshop.stats.service.StatsService;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/product/stats")
@Slf4j
@Validated
public class StatsController {

    private final StatsService statsService;

    //@PreAuthorize("hasAuthority('admin:write')")
    @GetMapping(path = "/admin/seller")
    public SellerReport getSellerReportAdmin(
            @RequestBody StatsFilterAdmin statsFilterAdmin,
            @RequestParam(name = "start") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate start,
            @RequestParam(name = "end") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end,
            @RequestParam(name = "sort", defaultValue = "quantity") String sort) {
        return statsService.getSellerReportAdmin(statsFilterAdmin, start, end, sort);
    }

    @PreAuthorize("hasAuthority('admin:write')")
    @GetMapping(path = "/admin/product")
    public List<SellerReport> getProductReportAdmin(
            @RequestBody StatsFilterAdmin statsFilterAdmin,
            @RequestParam(name = "start") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate start,
            @RequestParam(name = "end") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end,
            @RequestParam(name = "sort", defaultValue = "popular") String sort) {
        return statsService.getProductReportAdmin(statsFilterAdmin, start, end, sort);
    }

    @PreAuthorize("hasAuthority('seller:write')")
    @GetMapping(path = "/seller")
    public List<SellerReport> getProductsReportSeller(
            Principal principal,
            @RequestBody StatsFilterSeller statsFilterSeller,
            @RequestParam(name = "start") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate start,
            @RequestParam(name = "end") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end,
            @RequestParam(name = "sort", defaultValue = "popular") String sort) {
        return statsService.getProductsReportSeller(principal.getName(), statsFilterSeller, start, end, sort);
    }

    //@PreAuthorize("hasAuthority('seller:write')")
    @PostMapping
    public void createStats () {
        statsService.createStats();
    }
}