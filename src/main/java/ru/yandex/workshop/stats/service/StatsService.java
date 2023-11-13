package ru.yandex.workshop.stats.service;

import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.workshop.main.exception.NotValidValueException;
import ru.yandex.workshop.main.model.buyer.Order;
import ru.yandex.workshop.main.model.buyer.ProductOrder;
import ru.yandex.workshop.main.model.buyer.QOrder;
import ru.yandex.workshop.main.model.buyer.QProductOrder;
import ru.yandex.workshop.main.repository.buyer.OrderRepository;
import ru.yandex.workshop.main.repository.buyer.ProductOrderRepository;
import ru.yandex.workshop.main.util.QPredicates;
import ru.yandex.workshop.stats.dto.StatsFilterAdmin;
import ru.yandex.workshop.stats.dto.StatsFilterSeller;
import ru.yandex.workshop.stats.model.QStats;
import ru.yandex.workshop.stats.model.SellerReport;
import ru.yandex.workshop.stats.model.SellerReportEntry;
import ru.yandex.workshop.stats.model.Stats;
import ru.yandex.workshop.stats.repository.StatsRepository;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static ru.yandex.workshop.main.message.ExceptionMessage.NOT_VALID_VALUE_EXCEPTION;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StatsService {

    private final EntityManager entityManager;
    private final StatsRepository statsRepository;
    private final ProductOrderRepository productOrderRepository;
    private final OrderRepository orderRepository;

    private static final Float COMMISSIONS = 0.9F;

    public SellerReport getSellerReportAdmin(
            StatsFilterAdmin statsFilterAdmin,
            LocalDate start,
            LocalDate end,
            String sort) {
        if (end.isAfter(LocalDate.now()) || start.isAfter(end)) {
            throw new NotValidValueException(NOT_VALID_VALUE_EXCEPTION.label);
        }
        //        if (sort.equals(SortEnum.QUANTITY.toString().toLowerCase())) {
//            sort = valueOf(Sort.by(SortEnum.productAmount.toString().toLowerCase()));
//        }

        SellerReportEntry sellerReportEntry = new SellerReportEntry();
        QProductOrder productOrder = QProductOrder.productOrder;

        Predicate predicate = QPredicates.builder()
                .add(statsFilterAdmin.getCategoriesIds(), productOrder.product.category.id::in)
                .add(statsFilterAdmin.getSellerIds(), productOrder.product.seller.id::in)
                .add(statsFilterAdmin.getVendorIds(), productOrder.product.vendor.id::in)
                .buildAnd();

        List<Stats> statsList = statsRepository.findSellerReport(start, end, predicate)

        System.out.println();
        System.out.println();
        System.out.println(statsList.get(0));
        System.out.println(statsList.get(1));
        System.out.println(statsList.get(2));
        System.out.println();
        System.out.println();

        return null;
    }
//        List<Stats> fetch = new JPAQueryFactory(entityManager)
//                .select(qStats.id,
//                        qStats.buyer,
//                        qStats.product.name,
//                        qStats.dateBuy,
//                        qStats.quantity,
//                        qStats.amount)
//                .from(qStats)
//                .fetch()
//                .stream()
//                .map(tuple -> new Stats(
//                        tuple.get(qStats.id),
//                        tuple.get(qStats.buyer),
//                        tuple.get(qStats.product),
//                        LocalDate.from(tuple.get(qStats.dateBuy)),
//                        tuple.get(qStats.quantity),
//                        tuple.get(qStats.amount)))
//                .collect(Collectors.toList());

//        List<SellerReportEntry> statsList = new ArrayList<>();

//        for (int i = 0; i < fetch.size(); i++) {
//            SellerReportEntry sellerReportEntry = new SellerReportEntry();
//            int quantity = fetch.get(i).getQuantity();
//            float amount = fetch.get(i).getAmount();
//            if (fetch.get(i).getProduct().getSeller().getId() == fetch.get(i+1).getProduct().getSeller().getId()) {
//                quantity += fetch.get(i+1).getQuantity();
//                amount += fetch.get(i+1).getAmount();
//            }
//            sellerReportEntry.setQuantity(quantity);
//            sellerReportEntry.setRevenue(amount);
//            sellerReportEntry.setProductName(fetch.get(i).getProduct().getName());
//            statsList.add(sellerReportEntry);
//        }

//        for (Stats t : fetch) {
//            SellerReportEntry sellerReportEntry = new SellerReportEntry();
//            sellerReportEntry.setProductName(t.getProduct().getName());
//            sellerReportEntry.setQuantity(t.getQuantity());
//            sellerReportEntry.setRevenue(t.getAmount());
//        }

//        List<SellerReportEntry> statsList = statsRepository.findSellerReport(start, end, predicate);
//
//        System.out.println(statsList.get(0));

//        List<Stats> statsList = new JPAQueryFactory(entityManager)
//                .select(qStats.product, qStats.quantity, qStats.amount)
//                .from(qStats)
//                .where(qStats.product.seller.id > 1 );

        //Iterable<Order> orderList = orderRepository.findAll(predicate);

        // Нужно подумать над моделью Statictic: создать отдельную сущность Statistic для связи с БД
        // Как связать и нужно ли связывать эту сущность с sellerReport и sellerReportEntity

//        QProductOrder qProductOrder = QProductOrder.productOrder;
/*        Predicate predicate = QPredicates.builder()
                .add(statsFilterAdmin.getCategoriesIds(), qProductOrder.product.category.id::in)
                .add(statsFilterAdmin.getSellerIds(), qProductOrder.product.seller.id::in)
                .add(statsFilterAdmin.getVendorIds(), qProductOrder.product.vendor.id::in)
                .buildAnd();
        SellerReport sellerReport = new SellerReport();

        Page<ProductOrder> productOrderList = productOrderRepository.findAll(predicate);

        List<SellerReportEntry> sellerReportEntryList = new ArrayList<>();
        Float sumRevenue = 0F;
        for (ProductOrder productOrder : productOrderList) {
            SellerReportEntry sellerReportEntry = new SellerReportEntry();
            sellerReportEntry.setProductName(productOrder.getProduct().getName());
            sellerReportEntry.setQuantity(productOrder.getQuantity());
            sellerReportEntry.setRevenue(COMMISSIONS * (productOrder.getProductAmount() * sellerReportEntry.getQuantity()));
            sellerReportEntryList.add(sellerReportEntry);
            sumRevenue += sellerReportEntry.getRevenue();
        }
        sellerReport.setSellerReportEntryList(sellerReportEntryList);
        sellerReport.setSumRevenue(sumRevenue);
        return sellerReport;*/


    public List<SellerReport> getProductReportAdmin(
            StatsFilterAdmin statsFilterAdmin,
            LocalDate start,
            LocalDate end,
            String sort) {
        return null;
    }

    public List<SellerReport> getProductsReportSeller(
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
                stats.setDateBuy(order1.getProductionTime().toLocalDate());
                stats.setQuantity(po.getQuantity());
                stats.setAmount(COMMISSIONS * po.getProductAmount());
                statsRepository.save(stats);
            }
        }
    }
}