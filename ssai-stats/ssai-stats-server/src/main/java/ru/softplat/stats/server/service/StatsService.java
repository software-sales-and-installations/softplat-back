package ru.softplat.stats.server.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.softplat.stats.dto.SortEnum;
import ru.softplat.stats.dto.StatsFilterAdmin;
import ru.softplat.stats.server.dto.SellerReportEntryAdmin;
import ru.softplat.stats.server.model.SellerReport;
import ru.softplat.stats.server.model.Stats;
import ru.softplat.stats.server.repository.StatsRepository;
import ru.softplat.stats.server.util.ApachePOI;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
//@RequiredArgsConstructor
public class StatsService {

    private final StatsRepository statsRepository;
    private final StatBuyerService statBuyerService;

    public StatsService(StatsRepository statsRepository, StatBuyerService statBuyerService, StatProductService statProductService, StatSellerService statSellerService, ApachePOI apachePOI) {
        this.statsRepository = statsRepository;
        this.statBuyerService = statBuyerService;
        this.statProductService = statProductService;
        this.statSellerService = statSellerService;
        this.apachePOI = apachePOI;
    }

    private final StatProductService statProductService;
    private final StatSellerService statSellerService;

    private final ApachePOI apachePOI;

    @Value("${stats.commissionAdmin}")
    private Double commissionAdmin;
    @Value("${stats.commissionSeller}")
    private Double commissionSeller;

    @Transactional(readOnly = true)
    public SellerReport getSellerReportAdmin(
            StatsFilterAdmin statsFilterAdmin,
            SortEnum sort) throws IOException {
        List<SellerReportEntryAdmin> statsPage = getReportForAdminSellersList(statsFilterAdmin);
        SellerReport sellerReport = getSellerReport(sort, statsPage);
        apachePOI.createFileAdmin(sellerReport);
        return sellerReport;
    }

//    @Transactional(readOnly = true)
//    public SellerReport getProductReportAdmin(
//            StatsFilterSeller statsFilterSeller,
//            SortEnum sort) throws IOException {
//        List<SellerReportEntryAdmin> statsPage = getReportSellerList(statsFilterSeller, null);
//        SellerReport sellerReport = getSellerReport(sort, statsPage);
//        apachePOI.createFileAdmin(sellerReport);
//        return sellerReport;
//    }

/*    @Transactional(readOnly = true)
    public SellerReport getProductsReportSeller(
            Long sellerId,
            StatsFilterSeller statsFilterSeller,
            SortEnum sort) throws IOException {
        List<SellerReportEntrySeller> statsPage = getReportSellerList(statsFilterSeller, sellerId);
        //apachePOI.createFile(statsPage);
        SellerReport sellerReport = getSellerReport(sort, statsPage);
        apachePOI.createFileAdmin(sellerReport);
        return sellerReport;
    }*/

    private static SellerReport getSellerReport(SortEnum sort, List<SellerReportEntryAdmin> statsPage) {
        List<SellerReportEntryAdmin> sellerReportEntryList = statsPage
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
                        .map(SellerReportEntryAdmin::getRevenue)
                        .reduce(0D, Double::sum),
                sellerReportEntryList.stream()
                        .map(SellerReportEntryAdmin::getReceiveAmountAdmin)
                        .reduce(0D, Double::sum));
    }

    private List<SellerReportEntryAdmin> getReportForAdminSellersList(StatsFilterAdmin statsFilterAdmin) {
        if (statsFilterAdmin.getStart() == null) {
            statsFilterAdmin.setStart(LocalDateTime.now().minusMonths(3).withHour(0).withMinute(0).withSecond(0));
        }
        if (statsFilterAdmin.getEnd() == null) {
            statsFilterAdmin.setEnd(LocalDateTime.now());
        }
        List<SellerReportEntryAdmin> statsPage;
        if (statsFilterAdmin.getSellerIds() != null) {
            statsPage = statsRepository.getStatsByProductAllSeller(
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
        stats.setReceiveAmountAdmin((double) Math.round(commissionAdmin * stats.getAmount()));
        stats.setReceiveAmountSeller((double) Math.round(commissionSeller * stats.getAmount()));
        stats.setAmount(stats.getAmount());
        statBuyerService.createStatBuyer(stats.getBuyer());
        statSellerService.createStatSeller(stats.getProduct().getSeller());
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