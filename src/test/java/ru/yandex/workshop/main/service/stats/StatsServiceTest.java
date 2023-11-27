package ru.yandex.workshop.main.service.stats;

import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.yandex.workshop.main.model.buyer.BasketPosition;
import ru.yandex.workshop.main.model.buyer.Buyer;
import ru.yandex.workshop.main.model.buyer.Order;
import ru.yandex.workshop.main.model.buyer.OrderPosition;
import ru.yandex.workshop.main.model.image.Image;
import ru.yandex.workshop.main.model.product.Category;
import ru.yandex.workshop.main.model.product.License;
import ru.yandex.workshop.main.model.product.Product;
import ru.yandex.workshop.main.model.product.ProductStatus;
import ru.yandex.workshop.main.model.seller.Seller;
import ru.yandex.workshop.main.model.vendor.Country;
import ru.yandex.workshop.main.model.vendor.Vendor;
import ru.yandex.workshop.main.repository.buyer.OrderRepository;
import ru.yandex.workshop.main.service.buyer.OrderService;
import ru.yandex.workshop.stats.dto.SellerReportEntry;
import ru.yandex.workshop.stats.dto.StatsFilterAdmin;
import ru.yandex.workshop.stats.dto.StatsFilterSeller;
import ru.yandex.workshop.stats.model.SellerReport;
import ru.yandex.workshop.stats.model.SortEnum;
import ru.yandex.workshop.stats.model.Stats;
import ru.yandex.workshop.stats.repository.StatsRepository;
import ru.yandex.workshop.stats.service.StatsService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StatsServiceTest {
    @InjectMocks
    private StatsService statsService;
    @Mock(lenient = true)
    private OrderService orderService;
    @Mock
    private StatsRepository statsRepository;
    @Mock
    private OrderRepository orderRepository;
    private static Seller seller;
    private static Product product;
    private static Buyer buyer;
    private static BasketPosition basketPosition;
    private static OrderPosition orderPosition;
    private static Order order;
    private static StatsFilterAdmin statsFilterAdmin;
    private static StatsFilterSeller statsFilterSeller;
    private static SellerReport sellerReport;
    private static LocalDateTime localDateTime = LocalDateTime.now();

    @BeforeEach
    void init() {
        Image image = new Image(
                1L,
                "name",
                123L,
                "contentType",
                new byte[]{0x01, 0x02, 0x03});

        Vendor vendor = Vendor.builder()
                .id(1L)
                .name("name1")
                .description("Name One")
                .country(Country.RUSSIA)
                .build();

        seller = Seller.builder()
                .id(1L)
                .email("NameOne@gmail.com")
                .name("Name")
                .phone(" +79111111111")
                .registrationTime(LocalDateTime.now())
                .build();

        buyer = Buyer.builder()
                .id(1L)
                .email("NameTwo@gmail.com")
                .name("user")
                .phone("1234567890")
                .build();

        product = Product.builder()
                .id(2L)
                .name("pr")
                .category(
                        new Category(
                                1L,
                                "Category"
                        )
                )
                .description("descr")
                .license(License.LICENSE)
                .price(1234.2F)
                .seller(seller)
                .vendor(vendor)
                .version("2.2.2")
                .productionTime(LocalDateTime.now())
                .installation(true)
                .quantity(1234)
                .productStatus(ProductStatus.PUBLISHED)
                .image(image)
                .productAvailability(true)
                .installationPrice(10.00F)
                .build();

        basketPosition = new BasketPosition(
                1L,
                1L,
                product,
                2,
                true);

        orderPosition = OrderPosition.builder()
                .id(1L)
                .product(product)
                .quantity(2)
                .installation(true)
                .build();

        statsFilterAdmin = new StatsFilterAdmin(
                List.of(1L),
                localDateTime.minusMonths(3L),
                localDateTime);

        statsFilterSeller = new StatsFilterSeller(
                localDateTime.minusMonths(3L),
                localDateTime);

        SellerReportEntry sellerReportEntry = new SellerReportEntry(
                product.getName(),
                2L,
                (double) (product.getPrice() * 2L));

        sellerReport = new SellerReport(
                List.of(sellerReportEntry),
                sellerReportEntry.getRevenue());

        order = new Order(
                1L,
                localDateTime,
                buyer,
                new ArrayList<>(),
                12.12F);
    }

    @Test
    @SneakyThrows
    @DisplayName("Получение статистики адимном по конкретному продавцу")
    void getSellerReportAdminTest() {
        when(orderService.createOrder(anyString(), anyList())).thenReturn(order);
        when(statsRepository.getStatsByProduct(
                any(),
                any(),
                any()))
                .thenReturn(sellerReport.getSellerReportEntryList());
//строки для наглядности параметров
        List<Order> orderList = orderRepository.findAll();
        List<Stats> stats = statsRepository.findAll();
        System.out.println(orderList.size());
        System.out.println(stats.size());

        SellerReport response = statsService.getSellerReportAdmin(
                statsFilterAdmin,
                SortEnum.POPULAR);
        assertNotNull(response);
        assertEquals(sellerReport.getSumRevenue(), response.getSumRevenue());
        assertEquals(1, response.getSellerReportEntryList().size());
    }

    @Test
    @SneakyThrows
    @DisplayName("Получение статистики адимном по всем продавцам c null sellerIds")
    void getSellerReportAdminWithNullSellerIdTest() {
        StatsFilterAdmin statsFilterAdminNullSellerIds = new StatsFilterAdmin(
                null,
                localDateTime.minusMonths(3L),
                localDateTime);

        when(statsRepository.getAllStats(
                any(),
                any()))
                .thenReturn(sellerReport.getSellerReportEntryList());

        SellerReport response = statsService.getSellerReportAdmin(
                statsFilterAdminNullSellerIds,
                SortEnum.POPULAR);
        assertNotNull(response);
        assertEquals(sellerReport.getSumRevenue(), response.getSumRevenue());
        assertEquals(1, response.getSellerReportEntryList().size());
    }

    @Test
    @SneakyThrows
    @DisplayName("Получение статистики адимном по всем продавцам c empty sellerIds")
    void getSellerReportAdminWithEmptySellerIdTest() {
        StatsFilterAdmin statsFilterAdminNullSellerIds = new StatsFilterAdmin(
                new ArrayList<>(),
                localDateTime.minusMonths(3L),
                localDateTime);

        when(statsRepository.getAllStats(
                any(),
                any()))
                .thenReturn(sellerReport.getSellerReportEntryList());

        SellerReport response = statsService.getSellerReportAdmin(
                statsFilterAdminNullSellerIds,
                SortEnum.POPULAR);
        assertNotNull(response);
        assertEquals(sellerReport.getSumRevenue(), response.getSumRevenue());
        assertEquals(1, response.getSellerReportEntryList().size());
    }

    @Test
    @SneakyThrows
    @DisplayName("Получение статистики адимном по товару")
    void getProductReportAdminTest() {
        when(statsRepository.getAllStats(
                any(),
                any()))
                .thenReturn(sellerReport.getSellerReportEntryList());

        SellerReport response = statsService.getProductReportAdmin(
                statsFilterSeller,
                SortEnum.POPULAR);
        assertNotNull(response);
        assertEquals(sellerReport.getSumRevenue(), response.getSumRevenue());
        assertEquals(1, response.getSellerReportEntryList().size());
    }

    @Test
    @SneakyThrows
    @DisplayName("Получение статистики продавцом по своему товару")
    void getProductsReportSellerTest() {
        when(statsRepository.getAllStatsEmailSeller(
                any(),
                any(),
                any()))
                .thenReturn(sellerReport.getSellerReportEntryList());

        SellerReport response = statsService.getProductsReportSeller(
                seller.getEmail(),
                statsFilterSeller,
                SortEnum.POPULAR);

        assertNotNull(response);
        assertEquals(sellerReport.getSumRevenue(), response.getSumRevenue());
        assertEquals(1, response.getSellerReportEntryList().size());
    }
}
