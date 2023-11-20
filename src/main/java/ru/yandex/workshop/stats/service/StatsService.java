package ru.yandex.workshop.stats.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.workshop.stats.dto.SellerReportEntry;
import ru.yandex.workshop.stats.dto.StatsFilterAdmin;
import ru.yandex.workshop.stats.dto.StatsFilterSeller;
import ru.yandex.workshop.stats.model.SellerReport;
import ru.yandex.workshop.stats.model.SortEnum;
import ru.yandex.workshop.stats.model.Stats;
import ru.yandex.workshop.stats.repository.StatsRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class StatsService {

    private final StatsRepository statsRepository;

    @Transactional(readOnly = true)
    public SellerReport getSellerReportAdmin(
            StatsFilterAdmin statsFilterAdmin,
            SortEnum sort) {
        List<SellerReportEntry> statsPage = getReportForAdminSellersList(statsFilterAdmin);
        return getSellerReport(sort, statsPage);
    }

    @Transactional(readOnly = true)
    public SellerReport getProductReportAdmin(
            StatsFilterSeller statsFilterSeller,
            SortEnum sort) {
        List<SellerReportEntry> statsPage = getReportSellerList(statsFilterSeller, null);
        return getSellerReport(sort, statsPage);
    }

    @Transactional(readOnly = true)
    public SellerReport getProductsReportSeller(
            String email,
            StatsFilterSeller statsFilterSeller,
            SortEnum sort) {
        List<SellerReportEntry> statsPage = getReportSellerList(statsFilterSeller, email);
        return getSellerReport(sort, statsPage);
    }

    private static SellerReport getSellerReport(SortEnum sort, List<SellerReportEntry> statsPage) {
        List<SellerReportEntry> sellerReportEntryList = statsPage
                .stream()
                .sorted((o1, o2) -> {
                    if (SortEnum.POPULAR.equals(sort)) {
                        return Long.compare(o2.getQuantity(), o1.getQuantity());
                    } else {
                        return Double.compare(o2.getRevenue(), o1.getRevenue());
                    }
                })
                .collect(Collectors.toList());

        return new SellerReport(
                sellerReportEntryList,
                sellerReportEntryList.stream()
                        .map(SellerReportEntry::getRevenue)
                        .reduce(0D, Double::sum));
    }

    private List<SellerReportEntry> getReportForAdminSellersList(StatsFilterAdmin statsFilterAdmin) {
        if (statsFilterAdmin.getStart() == null) {
            statsFilterAdmin.setStart(LocalDateTime.now().minusMonths(3).withHour(0).withMinute(0).withSecond(0));
        }
        if (statsFilterAdmin.getEnd() == null) {
            statsFilterAdmin.setEnd(LocalDateTime.now().withHour(0).withMinute(0).withSecond(0));
        }
        List<SellerReportEntry> statsPage;
        if (statsFilterAdmin.getSellerIds() != null) {
            statsPage = statsRepository.getStatsByProduct(
                    statsFilterAdmin.getSellerIds(),
                    statsFilterAdmin.getStart(),
                    statsFilterAdmin.getEnd());
        } else {
            statsPage = statsRepository.getAllStats(
                    statsFilterAdmin.getStart(),
                    statsFilterAdmin.getEnd());
        }
        return statsPage;
    }

    private List<SellerReportEntry> getReportSellerList(StatsFilterSeller statsFilterSeller, String email) {
        if (statsFilterSeller.getStart() == null) {
            statsFilterSeller.setStart(LocalDateTime.now().minusMonths(3).withHour(0).withMinute(0).withSecond(0));
        }
        if (statsFilterSeller.getEnd() == null) {
            statsFilterSeller.setEnd(LocalDateTime.now().withHour(0).withMinute(0).withSecond(0));
        }
        List<SellerReportEntry> allStats;
        if (email == null) {
            allStats = statsRepository.getAllStats(
                    statsFilterSeller.getStart(),
                    statsFilterSeller.getEnd());
        } else {
            allStats = statsRepository.getAllStatsEmailSeller(
                    email,
                    statsFilterSeller.getStart(),
                    statsFilterSeller.getEnd());
        }
        return allStats;
    }

    public void createStats(Stats stats) {
        statsRepository.save(stats);
    }
}