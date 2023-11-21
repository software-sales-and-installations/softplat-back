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
import ru.yandex.workshop.main.model.buyer.Basket;
import ru.yandex.workshop.main.model.buyer.BasketPosition;
import ru.yandex.workshop.main.model.buyer.Buyer;
import ru.yandex.workshop.main.model.product.Category;
import ru.yandex.workshop.main.model.product.License;
import ru.yandex.workshop.main.model.product.Product;
import ru.yandex.workshop.main.model.product.ProductStatus;
import ru.yandex.workshop.main.model.seller.Seller;
import ru.yandex.workshop.main.model.vendor.Country;
import ru.yandex.workshop.main.model.vendor.Vendor;
import ru.yandex.workshop.main.repository.buyer.BasketPositionRepository;
import ru.yandex.workshop.main.repository.buyer.BasketRepository;
import ru.yandex.workshop.main.service.product.SearchProductService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BasketServiceTest {
    @InjectMocks
    private BasketService basketService;
    @Mock
    private BasketRepository basketRepository;
    @Mock
    private SearchProductService productService;
    @Mock
    private BasketPositionRepository basketPositionRepository;
    @Mock
    private BuyerService buyerService;
    @Captor
    private ArgumentCaptor<Basket> basketCaptor;
    @Captor
    private ArgumentCaptor<BasketPosition> basketPositionCaptor;

    private static Product product;
    private static Buyer buyer;
    private static final String email = "user@user.com";
    private static BasketPosition basketPosition;
    private static Basket basket;


    @BeforeEach
    void init() {
        buyer = Buyer.builder()
                .id(1L)
                .email(email)
                .name("user")
                .phone("1234567890")
                .build();

        Vendor vendor = Vendor.builder()
                .id(1L)
                .name("name1")
                .description("Name One")
                .country(Country.RUSSIA)
                .build();

        Seller seller = Seller.builder()
                .id(1L)
                .email("NameTwo@gmail.com")
                .name("Name")
                .phone("+79111111111")
                .registrationTime(LocalDateTime.now())
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
                .build();


        basketPosition = new BasketPosition(1L, 1L, product, 1, true);
        List<BasketPosition> basketPositionList = new ArrayList<>();
        basketPositionList.add(basketPosition);
        basket = new Basket(1L, 1L, basketPositionList);
    }

    @Test
    @SneakyThrows
    void getBasketTest_shouldReturnNewBasket_whenEmpty() {
        when(buyerService.getBuyerByEmail(anyString())).thenReturn(buyer);
        when(basketRepository.findByBuyerId(1L)).thenReturn(Optional.empty());
        when(basketRepository.save(any())).thenReturn(basket);

        basketService.getOrCreateBasket(email);
        verify(basketRepository).save(basketCaptor.capture());
        Basket actual = basketCaptor.getValue();

        assertEquals(1L, actual.getBuyerId());
        assertEquals(0, actual.getProductsInBasket().size());
    }

    @Test
    @SneakyThrows
    void addProductTest_shouldReturnBasketDto_whenHaveNoProducts() {
        when(buyerService.getBuyerByEmail(anyString())).thenReturn(buyer);
        when(productService.getProductById(2L)).thenReturn(product);
        when(basketRepository.findByBuyerId(1L)).thenReturn(Optional.of(basket));

        basketService.addProduct(email, 2L, true);
        verify(basketPositionRepository).save(basketPositionCaptor.capture());
        BasketPosition actual = basketPositionCaptor.getValue();

        assertEquals(1L, actual.getBasketId());
        assertEquals(product.getId(), actual.getProduct().getId());
        assertEquals(2, actual.getQuantity());
        assertEquals(true, actual.getInstallation());
    }

    @Test
    @SneakyThrows
    void addProductTest_shouldReturnBasketDto_whenHaveSameProduct() {
        when(buyerService.getBuyerByEmail(anyString())).thenReturn(buyer);
        when(productService.getProductById(2L)).thenReturn(product);
        when(basketRepository.findByBuyerId(1L)).thenReturn(Optional.of(basket));

        basketService.addProduct(email, 2L, true);
        verify(basketPositionRepository).save(basketPositionCaptor.capture());
        BasketPosition response = basketPositionCaptor.getValue();

        assertEquals(1L, response.getBasketId());
        assertEquals(product.getId(), response.getProduct().getId());
        assertEquals(2, response.getQuantity());
        assertEquals(true, response.getInstallation());
    }

    @Test
    @SneakyThrows
    void addProductTest_shouldReturnBasketDto_whenHaveSameProductWithoutInstallation() {
        when(buyerService.getBuyerByEmail(anyString())).thenReturn(buyer);
        when(productService.getProductById(2L)).thenReturn(product);
        when(basketRepository.findByBuyerId(1L)).thenReturn(Optional.of(basket));

        basketService.addProduct(email, 2L, false);
        verify(basketPositionRepository).save(basketPositionCaptor.capture());
        BasketPosition actual = basketPositionCaptor.getValue();

        assertEquals(1L, actual.getBasketId());
        assertEquals(product.getId(), actual.getProduct().getId());
        assertEquals(1, actual.getQuantity());
        assertEquals(false, actual.getInstallation());
    }

    @Test
    @SneakyThrows
    void removeProductTest_shouldReturnBasketDtoWithEmptyProductBaskets_whenHaveOneProduct() {
        when(buyerService.getBuyerByEmail(email)).thenReturn(buyer);
        when(basketRepository.findByBuyerId(1L)).thenReturn(Optional.of(basket));
        basketService.removeProduct(email, 2L, true);

        verify(basketPositionRepository).deleteById(1L);
    }

    @Test
    @SneakyThrows
    void removeProductTest_shouldReturnBasketDto_whenHaveManyProducts() {
        List<BasketPosition> basketPositionList = new ArrayList<>();
        basketPosition.setQuantity(3);
        basketPositionList.add(basketPosition);
        basket.setProductsInBasket(basketPositionList);

        when(buyerService.getBuyerByEmail(email)).thenReturn(buyer);
        when(basketRepository.findByBuyerId(1L)).thenReturn(Optional.of(basket));
        basketService.removeProduct(email, 2L, true);

        assertEquals(1L, basketPosition.getBasketId());
        assertEquals(product.getId(), basketPosition.getProduct().getId());
        assertEquals(2, basketPosition.getQuantity());
        assertEquals(true, basketPosition.getInstallation());
    }
}
