package ru.softplat.stats.server.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.softplat.stats.dto.ReportEntryDto;
import ru.softplat.stats.dto.SortEnum;
import ru.softplat.stats.dto.StatsFilter;
import ru.softplat.stats.server.mapper.ReportEntryMapper;
import ru.softplat.stats.server.mapper.StatsMapper;
import ru.softplat.stats.server.model.Report;
import ru.softplat.stats.server.model.ReportEntry;
import ru.softplat.stats.server.model.StatDemo;
import ru.softplat.stats.server.model.Stats;
import ru.softplat.stats.server.repository.StatDemoRepository;
import ru.softplat.stats.server.repository.StatsRepository;
import ru.softplat.stats.server.util.ApachePOI;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class StatsService {

    private final StatsRepository statsRepository;
    private final StatDemoRepository demoRepository;
    private final StatBuyerService statBuyerService;
    private final StatProductService statProductService;
    private final StatsMapper statsMapper;
    private final ReportEntryMapper reportEntryMapper;
    private final ApachePOI apachePOI;

    @Transactional(readOnly = true)
    public Report getSellerReportAdmin(StatsFilter filter, SortEnum sort) {
        List<ReportEntry> statsPage = getStatPage(filter, sort);
        return getReportAdmin(statsPage);
    }

    @Transactional(readOnly = true)
    public Report getProductReportSeller(StatsFilter filter, SortEnum sort) {
        List<ReportEntry> statsPage = getStatPage(filter, sort);
        return getReportSeller(statsPage);
    }

    private Report getReportAdmin(List<ReportEntry> statsPage) {
        List<ReportEntryDto> reportEntryList = statsPage
                .stream()
                .map(reportEntryMapper::reportEntryToAdminDto)
                .collect(Collectors.toList());

        return statsMapper.listEntriesToReport(reportEntryList);
    }

    private Report getReportSeller(List<ReportEntry> statsPage) {
        List<ReportEntryDto> reportEntryList = statsPage
                .stream()
                .map(r -> reportEntryMapper.reportEntryToSellerDto(r, countDemo(r.getProductName())))
                .collect(Collectors.toList());//TODO почему-то не показывает больше 1 демо

        return statsMapper.listEntriesToReport(reportEntryList);
    }

    private List<ReportEntry> getStatPage(StatsFilter statsFilter, SortEnum sort) {
        if (statsFilter.getSellerIds() != null) {
            switch (sort) {
                case POPULAR:
                    return statsRepository.getStatsBySellerIdsOrderByQuantity(
                            statsFilter.getSellerIds(),
                            statsFilter.getStart(),
                            statsFilter.getEnd());
                case PRICE:
                    return statsRepository.getStatsBySellerIdsOrderByPrice(
                            statsFilter.getSellerIds(),
                            statsFilter.getStart(),
                            statsFilter.getEnd());
            }
        } else {
            switch (sort) {
                case POPULAR:
                    return statsRepository.getAllStatsOrderByQuantity(
                            statsFilter.getStart(),
                            statsFilter.getEnd());
                case PRICE:
                    return statsRepository.getAllStatsOrderByPrice(
                            statsFilter.getStart(),
                            statsFilter.getEnd());
            }
        }
        throw new NullPointerException("Некорректные данные для отчета");
    }

    public void createStats(Stats stats) {
        statBuyerService.createStatBuyer(stats.getBuyer());
        statProductService.createStatProduct(stats.getProduct());
        statsRepository.save(stats);
    }

    public boolean saveAdminFile(StatsFilter filter, SortEnum sort) throws IOException {
        List<ReportEntry> statsPage = getStatPage(filter, sort);
        Report reportAdmin = getReportAdmin(statsPage);
        if (reportAdmin != null) {
            apachePOI.createFileAdmin(reportAdmin);
            return true;
        } else {
            return false;
        }
    }

    public boolean saveSellerFile(StatsFilter filter, SortEnum sort) throws IOException {
        List<ReportEntry> statsPage = getStatPage(filter, sort);
        Report reportSeller = getReportSeller(statsPage);
        if (reportSeller != null) {
            apachePOI.createFileSeller(reportSeller);
            return true;
        } else {
            return false;
        }
    }

    public void downloadDemo(StatDemo statDemo) {
        Optional<StatDemo> savedDemo = demoRepository.findByBuyerNameAndProductNameAndProductSellerName(
                statDemo.getBuyer().getName(),
                statDemo.getProduct().getName(),
                statDemo.getProduct().getSeller().getName());
        if (savedDemo.isEmpty()) {
            statDemo.setQuantity(1);
            statBuyerService.createStatBuyer(statDemo.getBuyer());
            statProductService.createStatProduct(statDemo.getProduct());
            demoRepository.save(statDemo);
        } else {
            savedDemo.get().setQuantity(savedDemo.get().getQuantity() + 1);
            demoRepository.save(savedDemo.get());
        }
    }

    private int countDemo(String productName) {
        return demoRepository.countAllByProductName(productName);
    }
}