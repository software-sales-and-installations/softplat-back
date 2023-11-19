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
import static org.junit.jupiter.api.Assertions.assertNull;
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
    }

    @Test
    @SneakyThrows
    void getBasketTest_whenEmpty_returnNewBasket() {
        when(buyerService.getBuyerByEmail(anyString())).thenReturn(buyer);
        when(basketRepository.findByBuyerId(1L)).thenReturn(Optional.empty());
        when(basketRepository.save(any())).thenReturn(new Basket(1L, 1L, null));

        basketService.getOrCreateBasket(email);

        verify(basketRepository).save(basketCaptor.capture());

        Basket response = basketCaptor.getValue();

        assertEquals(1L, response.getBuyerId());
        assertNull(response.getProductsInBasket());
    }

    @Test
    @SneakyThrows
    void addProductTest_whenHaveNoProducts_returnBasketDto() {
        when(buyerService.getBuyerByEmail(anyString())).thenReturn(buyer);
        when(productService.getProductById(2L)).thenReturn(product);
        when(basketRepository.findByBuyerId(1L)).thenReturn(Optional.of(new Basket(1L, 1L, null)));
        when(basketPositionRepository.findAllByBasketIdAndProduct_IdAndInstallation(1L, 2L, true))
                .thenReturn(null);

        basketService.addProduct(email, 2L, true);

        verify(basketPositionRepository).save(basketPositionCaptor.capture());

        BasketPosition response = basketPositionCaptor.getValue();

        assertEquals(1L, response.getBasketId());
        assertEquals(product.getId(), response.getProduct().getId());
        assertEquals(1, response.getQuantity());
        assertEquals(true, response.getInstallation());
    }

    @Test
    @SneakyThrows
    void addProductTest_whenHaveSameProduct_returnBasketDto() {
        when(buyerService.getBuyerByEmail(anyString())).thenReturn(buyer);
        when(productService.getProductById(2L)).thenReturn(product);
        when(basketRepository.findByBuyerId(1L)).thenReturn(Optional.of(new Basket(1L, 1L, null)));
        when(basketPositionRepository.findAllByBasketIdAndProduct_IdAndInstallation(1L, 2L, true))
                .thenReturn(basketPosition);

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
    void addProductTest_whenHaveSameProductWithoutInstallation_returnBasketDto() {
        when(buyerService.getBuyerByEmail(anyString())).thenReturn(buyer);
        when(productService.getProductById(2L)).thenReturn(product);
        when(basketRepository.findByBuyerId(1L)).thenReturn(Optional.of(new Basket(1L, 1L, null)));
        when(basketPositionRepository.findAllByBasketIdAndProduct_IdAndInstallation(1L, 2L, false))
                .thenReturn(null);

        basketService.addProduct(email, 2L, false);

        verify(basketPositionRepository).save(basketPositionCaptor.capture());

        BasketPosition response = basketPositionCaptor.getValue();

        assertEquals(1L, response.getBasketId());
        assertEquals(product.getId(), response.getProduct().getId());
        assertEquals(1, response.getQuantity());
        assertEquals(false, response.getInstallation());
    }

    @Test
    @SneakyThrows
    void removeProductTest_whenHaveOneProduct_returnBasketDtoWithEmptyProductBaskets() {
        List<BasketPosition> productBaskets = new ArrayList<>();
        productBaskets.add(basketPosition);

        when(buyerService.getBuyerByEmail(email)).thenReturn(buyer);
        when(basketRepository.findByBuyerId(1L)).thenReturn(Optional.of(new Basket(
                1L, 1L, productBaskets)));
        when(basketPositionRepository.findAllByBasketIdAndProduct_IdAndInstallation(1L, 2L, true))
                .thenReturn(basketPosition);

        basketService.removeProduct(email, 2L, true);

        verify(basketPositionRepository).deleteById(1L);
    }

    @Test
    @SneakyThrows
    void removeProductTest_whenHaveManyProducts_returnBasketDto() {
        List<BasketPosition> productBaskets = new ArrayList<>();
        basketPosition.setQuantity(3);
        productBaskets.add(basketPosition);

        when(buyerService.getBuyerByEmail(email)).thenReturn(buyer);
        when(basketRepository.findByBuyerId(1L)).thenReturn(Optional.of(new Basket(
                1L, 1L, productBaskets)));
        when(basketPositionRepository.findAllByBasketIdAndProduct_IdAndInstallation(1L, 2L, true))
                .thenReturn(basketPosition);

        basketService.removeProduct(email, 2L, true);

        verify(basketPositionRepository).save(basketPositionCaptor.capture());

        BasketPosition response = basketPositionCaptor.getValue();

        assertEquals(1L, response.getBasketId());
        assertEquals(product.getId(), response.getProduct().getId());
        assertEquals(2, response.getQuantity());
        assertEquals(true, response.getInstallation());
    }
}
