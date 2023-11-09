package ru.yandex.workshop.stats.service;

import com.querydsl.core.types.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.workshop.configuration.PageRequestOverride;
import ru.yandex.workshop.main.exception.NotValidValueException;
import ru.yandex.workshop.main.model.buyer.ProductOrder;
import ru.yandex.workshop.main.model.buyer.QProductOrder;
import ru.yandex.workshop.main.model.product.QCategory;
import ru.yandex.workshop.main.model.seller.QSeller;
import ru.yandex.workshop.main.model.vendor.QVendor;
import ru.yandex.workshop.main.repository.buyer.ProductOrderRepository;
import ru.yandex.workshop.main.util.QPredicates;
import ru.yandex.workshop.stats.dto.StatsFilterAdmin;
import ru.yandex.workshop.stats.dto.StatsFilterSeller;
import ru.yandex.workshop.stats.model.SellerReport;
import ru.yandex.workshop.stats.model.SellerReportEntry;
import ru.yandex.workshop.stats.model.SortEnum;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static java.lang.String.valueOf;
import static ru.yandex.workshop.main.message.ExceptionMessage.NOT_VALID_VALUE_EXCEPTION;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StatsService {

    private final ProductOrderRepository productOrderRepository;
    private static final Float COMMISSIONS = 0.9F;

    public SellerReport getSellerReportAdmin(
            StatsFilterAdmin statsFilterAdmin,
            LocalDate start, // вот здесь проблема, у нас в ProductOrder нету даты
            LocalDate end,   // и тут соответственно
            String sort,
            int from,       // пагинация для статистики не нужна
            int size) {     // пагинация для статистики не нужна

//        if (sort.equals(SortEnum.QUANTITY.toString().toLowerCase())) {
//            sort = valueOf(Sort.by(SortEnum.productAmount.toString().toLowerCase()));
//        }
        PageRequest pageRequest = PageRequestOverride.of(from, size/*, Sort.by(sort)*/);
        if (end.isBefore(LocalDate.now()) || start.isAfter(end)) {
            throw new NotValidValueException(NOT_VALID_VALUE_EXCEPTION.label);
        }
        QProductOrder qProductOrder = QProductOrder.productOrder;

        Predicate predicate = QPredicates.builder()
                .add(statsFilterAdmin.getCategoriesIds(), qProductOrder.product.category.id::in)
                .add(statsFilterAdmin.getSellerIds(), qProductOrder.product.seller.id::in)
                .add(statsFilterAdmin.getVendorIds(), qProductOrder.product.vendor.id::in)
                .buildAnd();
        SellerReport sellerReport = new SellerReport();

        Page<ProductOrder> productOrderList = productOrderRepository.findAll(predicate, pageRequest);

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
        return sellerReport;
    }

    public List<SellerReport> getProductReportAdmin(
            StatsFilterAdmin statsFilterAdmin,
            LocalDate start,
            LocalDate end,
            String sort,
            int from,
            int size) {
        return null;
    }

    public List<SellerReport> getProductsReportSeller(
            String email,
            StatsFilterSeller statsFilterSeller,
            LocalDate start,
            LocalDate end,
            String sort,
            int from,
            int size) {
        return null;
    }
}
