package ru.yandex.workshop.stats.service;

import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.workshop.stats.dto.SellerReportEntry;
import ru.yandex.workshop.stats.dto.StatsFilterAdmin;
import ru.yandex.workshop.stats.dto.StatsFilterSeller;
import ru.yandex.workshop.stats.model.*;
import ru.yandex.workshop.stats.repository.StatsRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class StatsService {

    private final StatsRepository statsRepository;

    @Transactional(readOnly = true)
    public SellerReport getSellerReportAdmin(
            StatsFilterAdmin statsFilterAdmin,
            String sort) {
        List<Stats> statsPage = getAdminFilterForStatsList(statsFilterAdmin);
        return getSellerReport(sort, statsPage);
    }

    @Transactional(readOnly = true)
    public SellerReport getProductReportAdmin(
            StatsFilterSeller statsFilterSeller,
            String sort) {
        List<Stats> statsPage = getSellerFilterForStatsList(statsFilterSeller);
        return getSellerReport(sort, statsPage);
    }

    @Transactional(readOnly = true)
    public SellerReport getProductsReportSeller(
            String email,
            StatsFilterSeller statsFilterSeller,
            String sort) {

        List<Stats> statsPage = getSellerFilterForStatsList(statsFilterSeller);
        List<SellerReportEntry> sellerReportEntryList = statsPage
                .stream()
                .filter(stats -> stats.getProduct().getSeller().getEmail().equals(email))
                .map(stats -> {
                    return new SellerReportEntry(
                            stats.getProduct().getName(),
                            stats.getQuantity(),
                            stats.getAmount());
                })
                .collect(Collectors.toMap(
                        SellerReportEntry::getProductName,
                        Function.identity(),
                        (entry1, entry2) -> new SellerReportEntry(
                                entry1.getProductName(),
                                entry1.getQuantity() + entry2.getQuantity(),
                                entry1.getRevenue() + entry2.getRevenue()
                        ))
                )
                .values()
                .stream()
                .sorted((o1, o2) -> {
                    if (SortEnum.POPULAR.toString().equalsIgnoreCase(sort)) {
                        return Integer.compare(o2.getQuantity(), o1.getQuantity());
                    } else if (SortEnum.PRICE.toString().equalsIgnoreCase(sort)) {
                        return Double.compare(o2.getRevenue(), o1.getRevenue());
                    }
                    return 0;
                })
                .collect(Collectors.toList());

        return new SellerReport(
                sellerReportEntryList,
                sellerReportEntryList.stream()
                        .map(SellerReportEntry::getRevenue)
                        .reduce(0F, Float::sum));
    }

    public void createStats(Stats stats) {
                statsRepository.save(stats);
    }

    private List<Stats> getAdminFilterForStatsList(StatsFilterAdmin statsFilterAdmin) {
        QStats qStats = QStats.stats;
        List<BooleanExpression> booleanExpressions = new ArrayList<>();

        if (statsFilterAdmin.hasCategories()) {
            for (Long id : statsFilterAdmin.getCategoriesIds()) {
                booleanExpressions.add(qStats.product.category.id.eq(id));
            }
        }
        if (statsFilterAdmin.hasSeller()) {
            for (Long id : statsFilterAdmin.getSellerIds()) {
                booleanExpressions.add(qStats.product.seller.id.eq(id));
            }
        }
        if (statsFilterAdmin.hasVendor()) {
            for (Long id : statsFilterAdmin.getVendorIds()) {
                booleanExpressions.add(qStats.product.vendor.id.eq(id));
            }
        }
        if (statsFilterAdmin.getStart() != null &&
                statsFilterAdmin.getEnd() != null) {
            booleanExpressions.add(
                    qStats.dateBuy.between(
                            statsFilterAdmin.getStart(),
                            statsFilterAdmin.getEnd()));
        }

        List<Stats> statsPage;
        if (!booleanExpressions.isEmpty()) {
            BooleanExpression finalBooleanExp = booleanExpressions
                    .stream()
                    .reduce(BooleanExpression::and)
                    .get();
                statsPage = statsRepository.findAll(finalBooleanExp);
        } else {
            statsPage = statsRepository.findAll();
        }
        return statsPage;
    }

    private List<Stats> getSellerFilterForStatsList(StatsFilterSeller statsFilterSeller) {
        QStats qStats = QStats.stats;
        List<BooleanExpression> booleanExpressions = new ArrayList<>();

        if (statsFilterSeller.hasCategories()) {
            for (Long id : statsFilterSeller.getCategoriesIds()) {
                booleanExpressions.add(qStats.product.category.id.eq(id));
            }
        }
        if (statsFilterSeller.hasVendor()) {
            for (Long id : statsFilterSeller.getVendorIds()) {
                booleanExpressions.add(qStats.product.vendor.id.eq(id));
            }
        }
        if (statsFilterSeller.getStart() != null &&
                statsFilterSeller.getEnd() != null) {
            booleanExpressions.add(
                    qStats.dateBuy.between(
                            statsFilterSeller.getStart(),
                            statsFilterSeller.getEnd()));
        }

        List<Stats> statsPage;
        if (!booleanExpressions.isEmpty()) {
            BooleanExpression finalBooleanExp = booleanExpressions
                    .stream()
                    .reduce(BooleanExpression::and)
                    .get();
            statsPage = statsRepository.findAll(finalBooleanExp);
        } else {
            statsPage = statsRepository.findAll();
        }
        return statsPage;
    }

    private SellerReport getSellerReport(String sort, List<Stats> statsPage) {
        List<SellerReportEntry> sellerReportEntryList = statsPage
                .stream()
                .map(stats -> {
                    return new SellerReportEntry(
                            stats.getProduct().getName(),
                            stats.getQuantity(),
                            stats.getAmount());
                })
                .collect(Collectors.toMap(
                        SellerReportEntry::getProductName,
                        Function.identity(),
                        (entry1, entry2) -> new SellerReportEntry(
                                entry1.getProductName(),
                                entry1.getQuantity() + entry2.getQuantity(),
                                entry1.getRevenue() + entry2.getRevenue()
                        ))
                )
                .values()
                .stream()
                .sorted((o1, o2) -> {
                    if (SortEnum.POPULAR.toString().equalsIgnoreCase(sort)) {
                        return Integer.compare(o2.getQuantity(), o1.getQuantity());
                    } else if (SortEnum.PRICE.toString().equalsIgnoreCase(sort)) {
                        return Double.compare(o2.getRevenue(), o1.getRevenue());
                    }
                    return 0;
                })
                .collect(Collectors.toList());

        return new SellerReport(
                sellerReportEntryList,
                sellerReportEntryList.stream()
                        .map(SellerReportEntry::getRevenue)
                        .reduce(0F, Float::sum));
    }
}