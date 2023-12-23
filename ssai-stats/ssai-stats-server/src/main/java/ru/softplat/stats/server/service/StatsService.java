package ru.softplat.stats.server.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.softplat.stats.dto.SortEnum;
import ru.softplat.stats.dto.StatsFilterAdmin;
import ru.softplat.stats.dto.StatsFilterSeller;
import ru.softplat.stats.server.dto.ReportEntry;
import ru.softplat.stats.server.mapper.StatsMapper;
import ru.softplat.stats.server.model.SellerReport;
import ru.softplat.stats.server.model.Stats;
import ru.softplat.stats.server.repository.StatsRepository;
import ru.softplat.stats.server.util.ApachePOI;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class StatsService {

    private final StatsRepository statsRepository;
    private final StatBuyerService statBuyerService;
    private final StatProductService statProductService;
    private final StatsMapper statsMapper;
    private final ApachePOI apachePOI;

    @Transactional(readOnly = true)
    public SellerReport getSellerReportAdmin(StatsFilterAdmin statsFilterAdmin, SortEnum sort) throws IOException {
        List<ReportEntry> statsPage = getStatPage(statsFilterAdmin);
        SellerReport sellerReport = getReport(sort, statsPage);
        //apachePOI.createFileAdmin(sellerReport);
        return sellerReport;
    }

//    @Transactional(readOnly = true)
//    public SellerReport getProductReportAdmin(
//            StatsFilterSeller statsFilterSeller,
//            SortEnum sort) throws IOException {
//        List<SellerReportEntryAdmin> statsPage = getReportSellerList(statsFilterSeller, null);
//        SellerReport sellerReport = getReport(sort, statsPage);
//        apachePOI.createFileAdmin(sellerReport);
//        return sellerReport;
//    }

    @Transactional(readOnly = true)
    public SellerReport getProductReportSeller(
            Long sellerId,
            StatsFilterSeller statsFilterSeller,
            SortEnum sort) throws IOException {
        StatsFilterAdmin filter = StatsFilterAdmin.builder()
                .start(statsFilterSeller.getStart())
                .end(statsFilterSeller.getEnd())
                .sellerIds(List.of(sellerId))
                .build();
        List<ReportEntry> statsPage = getStatPage(filter);
        //apachePOI.createFile(statsPage);
        SellerReport sellerReport = getReport(sort, statsPage);
        //apachePOI.createFileAdmin(sellerReport);
        return sellerReport;
    }

    private SellerReport getReport(SortEnum sort, List<ReportEntry> statsPage) {
        List<ReportEntry> reportEntryList = statsPage
                .stream()
                .sorted((o1, o2) -> {
                    if (SortEnum.POPULAR.equals(sort)) {
                        return Long.compare(o2.getQuantity(), o1.getQuantity());
                    } else {
                        return Double.compare(o2.getCommonProfit(), o1.getCommonProfit());
                    }
                })
                .collect(Collectors.toList()); //TODO sort in repository

        return statsMapper.listEntriesToSellerReport(reportEntryList);
    }

    private List<ReportEntry> getStatPage(StatsFilterAdmin statsFilterAdmin) {
        List<ReportEntry> statsPage;
        if (statsFilterAdmin.getSellerIds() != null) {
            statsPage = statsRepository.getStatsBySellerIds(
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

/*    private List<SellerReportEntrySeller> getReportSellerList(StatsFilterSeller statsFilterSeller, Long sellerId) {
        if (statsFilterSeller.getStart() == null) {
            statsFilterSeller.setStart(LocalDateTime.now().minusMonths(3).withHour(0).withMinute(0).withSecond(0));
        }
        if (statsFilterSeller.getEnd() == null) {
            statsFilterSeller.setEnd(LocalDateTime.now());
        }
        List<SellerReportEntryAdmin> allStats;
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
    }*/

    public void createStats(Stats stats) {
        statBuyerService.createStatBuyer(stats.getBuyer());
        statProductService.createStatProduct(stats.getProduct());
        statsRepository.save(stats);
    }

    public boolean saveAdminFile() {
        return false;
    }

    public boolean saveSellerFile() {
        return false;
    }

    public void downloadDemo(Long productId) {

    }
}