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
import ru.yandex.workshop.main.dto.basket.OrderResponseDto;
import ru.yandex.workshop.main.dto.basket.OrderToCreateDto;
import ru.yandex.workshop.main.model.buyer.Buyer;
import ru.yandex.workshop.main.model.buyer.Order;
import ru.yandex.workshop.main.model.buyer.ProductBasket;
import ru.yandex.workshop.main.model.image.Image;
import ru.yandex.workshop.main.model.product.Category;
import ru.yandex.workshop.main.model.product.License;
import ru.yandex.workshop.main.model.product.Product;
import ru.yandex.workshop.main.model.product.ProductStatus;
import ru.yandex.workshop.main.model.seller.BankRequisites;
import ru.yandex.workshop.main.model.seller.Seller;
import ru.yandex.workshop.main.model.vendor.Country;
import ru.yandex.workshop.main.model.vendor.Vendor;
import ru.yandex.workshop.main.repository.buyer.BuyerRepository;
import ru.yandex.workshop.main.repository.buyer.OrderRepository;
import ru.yandex.workshop.main.repository.buyer.ProductBasketRepository;

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
    private ProductBasketRepository productBasketRepository;
    @Mock
    private BuyerRepository buyerRepository;
    @Captor
    private ArgumentCaptor<Order> argumentCaptor;

    private static Product product;
    private static OrderToCreateDto orderToCreateDto;
    private static Buyer buyer;
    private static final String email = "NameTwo@gmail.com";
    private static ProductBasket productBasket;
    private static Order order;


    @BeforeEach
    void init() {
        BankRequisites bankRequisites = new BankRequisites(
                1L,
                "1111 2222 3333 4444");

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
                .description("Description seller")
                .registrationTime(LocalDateTime.now())
                .requisites(bankRequisites)
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

        orderToCreateDto = new OrderToCreateDto(List.of(1L));

        productBasket = new ProductBasket(1L, product, 2, true);

        order = new Order(1L, LocalDateTime.now(), buyer, new ArrayList<>(), 12.12F);
    }

    @Test
    @SneakyThrows
    void createOrderTest() {
        when(buyerRepository.findByEmail(email)).thenReturn(Optional.of(buyer));
        when(productBasketRepository.findById(1L)).thenReturn(Optional.of(productBasket));
        when(orderRepository.save(any())).thenReturn(order);

        OrderResponseDto responseDto = orderService.createOrder(email, orderToCreateDto);

        verify(orderRepository).save(argumentCaptor.capture());

        Order captorValue = argumentCaptor.getValue();

        assertEquals(1L, captorValue.getBuyer().getId());
        assertEquals(email, captorValue.getBuyer().getEmail());
        assertEquals(buyer.getName(), captorValue.getBuyer().getName());
        assertEquals(product.getId(), captorValue.getProductsOrdered().get(0).getProduct().getId());
        assertEquals(productBasket.getQuantity(), captorValue.getProductsOrdered().get(0).getQuantity());
        assertEquals(productBasket.getInstallation(), captorValue.getProductsOrdered().get(0).getInstallation());
        assertEquals(product.getPrice() + product.getInstallationPrice(), captorValue.getProductsOrdered().get(0).getProductAmount());
        assertEquals((product.getPrice() + product.getInstallationPrice()) * 2, captorValue.getOrderAmount());

        assertEquals(order.getOrderAmount(), responseDto.getOrderAmount());
        assertEquals(order.getBuyer().getName(), responseDto.getBuyer().getName());
        assertEquals(order.getProductionTime(), responseDto.getProductionTime());
    }

    @Test
    @SneakyThrows
    void getOrderTest() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        OrderResponseDto responseDto = orderService.getOrder(email, 1L);

        assertEquals(order.getId(), responseDto.getId());
        assertEquals(order.getProductionTime(), responseDto.getProductionTime());
        assertEquals(order.getBuyer().getEmail(), responseDto.getBuyer().getEmail());
    }

    @Test
    @SneakyThrows
    void getAllOrdersTest() {
        when(buyerRepository.findByEmail(email)).thenReturn(Optional.of(buyer));
        when(orderRepository.findAllByBuyer_Id(anyLong(), any())).thenReturn(List.of(order));

        List<OrderResponseDto> responseDto = orderService.getAllOrders(email);

        assertEquals(order.getId(), responseDto.get(0).getId());
        assertEquals(order.getProductionTime(), responseDto.get(0).getProductionTime());
        assertEquals(order.getBuyer().getEmail(), responseDto.get(0).getBuyer().getEmail());
    }
}

