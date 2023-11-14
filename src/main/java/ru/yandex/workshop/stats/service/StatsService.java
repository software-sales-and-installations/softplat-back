package ru.yandex.workshop.stats.service;

import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.workshop.main.exception.NotValidValueException;
import ru.yandex.workshop.main.model.buyer.Order;
import ru.yandex.workshop.main.model.buyer.ProductOrder;
import ru.yandex.workshop.main.model.buyer.QOrder;
import ru.yandex.workshop.main.model.buyer.QProductOrder;
import ru.yandex.workshop.main.repository.buyer.OrderRepository;
import ru.yandex.workshop.stats.dto.StatsFilterAdmin;
import ru.yandex.workshop.stats.dto.StatsFilterSeller;
import ru.yandex.workshop.stats.model.QStats;
import ru.yandex.workshop.stats.model.SellerReport;
import ru.yandex.workshop.stats.model.SellerReportEntry;
import ru.yandex.workshop.stats.model.Stats;
import ru.yandex.workshop.stats.repository.StatsRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static ru.yandex.workshop.main.message.ExceptionMessage.NOT_VALID_VALUE_EXCEPTION;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StatsService {

    private final StatsRepository statsRepository;
    private final OrderRepository orderRepository;

    private static final Float COMMISSIONS = 0.9F;

    public SellerReport getSellerReportAdmin(
            StatsFilterAdmin statsFilterAdmin/*,
            String sort*/) {

//        if (!sort.equals(SortEnum.QUANTITY.toString().toLowerCase())) {
//            sort = SortEnum.AMOUNT.toString().toLowerCase();
//        }

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
                        .collect(Collectors.toList());

        return new SellerReport(
                sellerReportEntryList,
                sellerReportEntryList.stream()
                        .map(SellerReportEntry::getRevenue)
                        .reduce(0F, Float::sum));
    }

    public SellerReport getProductReportAdmin(
            StatsFilterAdmin statsFilterAdmin,
            LocalDate start,
            LocalDate end,
            String sort) {
        return null;
    }

    public SellerReport getProductsReportSeller(
            String email,
            StatsFilterSeller statsFilterSeller,
            LocalDate start,
            LocalDate end,
            String sort) {
        return null;
    }

    @Transactional
    public void createStats() {
        List<Order> ordersList = orderRepository.findAll();
        if (!ordersList.isEmpty()) {
            statsRepository.deleteAll();
        }
        for (Order order1 : ordersList) {
            List<ProductOrder> productOrderList = order1.getProductsOrdered();
            for (ProductOrder po : productOrderList) {
                Stats stats = new Stats();
                stats.setProduct(po.getProduct());
                stats.setBuyer(order1.getBuyer());
                stats.setDateBuy(order1.getProductionTime());
                stats.setQuantity(po.getQuantity());
                stats.setAmount(COMMISSIONS * po.getProductAmount());
                statsRepository.save(stats);
            }
        }
    }
}