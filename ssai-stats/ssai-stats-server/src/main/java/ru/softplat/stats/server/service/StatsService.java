package ru.softplat.stats.server.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.softplat.stats.dto.SortEnum;
import ru.softplat.stats.dto.StatsFilterAdmin;
import ru.softplat.stats.dto.StatsFilterSeller;
import ru.softplat.stats.server.dto.SellerReportEntry;
import ru.softplat.stats.server.model.SellerReport;
import ru.softplat.stats.server.model.Stats;
import ru.softplat.stats.server.repository.StatsRepository;
import ru.softplat.stats.server.util.ApachePOI;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class StatsService {

    private final StatsRepository statsRepository;
    private final StatBuyerService statBuyerService;
    private final StatProductService statProductService;
    private final StatSellerService statSellerService;

    private ApachePOI apachePOI;
    private static final Float COMMISSIONS = 0.9F;

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
            Long sellerId,
            StatsFilterSeller statsFilterSeller,
            SortEnum sort) throws IOException {
        List<SellerReportEntry> statsPage = getReportSellerList(statsFilterSeller, sellerId);
        //apachePOI.createFile(statsPage);
        SellerReport sellerReport = getSellerReport(sort, statsPage);
        apachePOI.createFile(sellerReport);
        return sellerReport;
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

    private List<SellerReportEntry> getReportSellerList(StatsFilterSeller statsFilterSeller, Long sellerId) {
        if (statsFilterSeller.getStart() == null) {
            statsFilterSeller.setStart(LocalDateTime.now().minusMonths(3).withHour(0).withMinute(0).withSecond(0));
        }
        if (statsFilterSeller.getEnd() == null) {
            statsFilterSeller.setEnd(LocalDateTime.now().withHour(0).withMinute(0).withSecond(0));
        }
        List<SellerReportEntry> allStats;
        if (sellerId == null) {
            allStats = statsRepository.getAllStats(
                    statsFilterSeller.getStart(),
                    statsFilterSeller.getEnd());
        } else {
            allStats = statsRepository.getStatsByProduct(
                    Collections.singletonList(sellerId),
                    statsFilterSeller.getStart(),
                    statsFilterSeller.getEnd());
        }
        return allStats;
    }

    public void createStats(Stats stats) {
        stats.setAmount(COMMISSIONS * stats.getAmount());
        statsRepository.save(stats);
        statBuyerService.createStatBuyer(stats.getBuyer());
        statProductService.createStatProduct(stats.getProduct());
        statSellerService.createStatSeller(stats.getProduct().getSeller());
    }
}