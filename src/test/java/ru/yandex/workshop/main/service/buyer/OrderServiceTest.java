package ru.yandex.workshop.main.service.buyer;

import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.yandex.workshop.main.mapper.OrderPositionMapper;
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
import ru.yandex.workshop.main.repository.buyer.BasketPositionRepository;
import ru.yandex.workshop.main.repository.buyer.OrderRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
    @InjectMocks
    private OrderService orderService;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private BuyerService buyerService;
    @Mock
    private BasketPositionRepository basketPositionRepository;
    @Mock
    private OrderPositionMapper mapper;
    @Captor
    private ArgumentCaptor<Order> argumentCaptor;

    private static Product product;
    private static Buyer buyer;
    private static final String email = "NameTwo@gmail.com";
    private static BasketPosition basketPosition;
    private static OrderPosition orderPosition;
    private static Order order;


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

        Seller seller = Seller.builder()
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

        Category category = new Category(
                1L,
                "Category");

        product = Product.builder()
                .id(2L)
                .name("pr")
                .category(category)
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

        basketPosition = new BasketPosition(1L, 1L, product, 2, true);

        orderPosition = OrderPosition.builder()
                .id(1L)
                .product(product)
                .quantity(2)
                .installation(true)
                .build();

        order = new Order(1L, LocalDateTime.now(), buyer, new ArrayList<>(), 12.12F);
    }

    @Test
    @SneakyThrows
    void createOrderTest() {
        when(buyerService.getBuyerByEmail(email)).thenReturn(buyer);
        when(basketPositionRepository.findById(1L)).thenReturn(Optional.of(basketPosition));
        when(orderRepository.save(any())).thenReturn(order);
        when(mapper.basketPositionToOrderPosition(any())).thenReturn(orderPosition);

        Order response = orderService.createOrder(email, List.of(1L));

        verify(orderRepository).save(argumentCaptor.capture());

        Order captorValue = argumentCaptor.getValue();

        assertEquals(1L, captorValue.getBuyer().getId());
        assertEquals(email, captorValue.getBuyer().getEmail());
        assertEquals(buyer.getName(), captorValue.getBuyer().getName());
        assertEquals(product.getId(), captorValue.getProductsOrdered().get(0).getProduct().getId());
        assertEquals(basketPosition.getQuantity(), captorValue.getProductsOrdered().get(0).getQuantity());
        assertEquals(basketPosition.getInstallation(), captorValue.getProductsOrdered().get(0).getInstallation());
        assertEquals(product.getPrice() + product.getInstallationPrice(), captorValue.getProductsOrdered().get(0).getProductAmount());
        assertEquals((product.getPrice() + product.getInstallationPrice()) * 2, captorValue.getOrderAmount());

        assertEquals(order.getOrderAmount(), response.getOrderAmount());
        assertEquals(order.getBuyer().getName(), response.getBuyer().getName());
        assertEquals(order.getProductionTime(), response.getProductionTime());
    }

    @Test
    @SneakyThrows
    void getOrderTest() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        Order response = orderService.getOrder(1L);

        assertEquals(order.getId(), response.getId());
        assertEquals(order.getProductionTime(), response.getProductionTime());
        assertEquals(order.getBuyer().getEmail(), response.getBuyer().getEmail());
    }

    @Test
    @SneakyThrows
    void getAllOrdersTest() {
        when(buyerService.getBuyerByEmail(email)).thenReturn(buyer);
        when(orderRepository.findAllByBuyer_Id(anyLong(), any())).thenReturn(List.of(order));

        List<Order> response = orderService.getAllOrders(email);

        assertEquals(order.getId(), response.get(0).getId());
        assertEquals(order.getProductionTime(), response.get(0).getProductionTime());
        assertEquals(order.getBuyer().getEmail(), response.get(0).getBuyer().getEmail());
    }
}

