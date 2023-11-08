package ru.yandex.workshop.main.service.buyer;

import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.workshop.main.model.buyer.Basket;
import ru.yandex.workshop.main.model.buyer.Buyer;
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
import ru.yandex.workshop.main.repository.buyer.BasketRepository;
import ru.yandex.workshop.main.repository.buyer.BuyerRepository;
import ru.yandex.workshop.main.repository.buyer.ProductBasketRepository;
import ru.yandex.workshop.main.repository.product.ProductRepository;
import ru.yandex.workshop.security.model.Role;
import ru.yandex.workshop.security.model.Status;

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

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class BasketServiceTest {
    @InjectMocks
    private BasketService basketService;
    @Mock
    private BasketRepository basketRepository;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private BuyerRepository buyerRepository;
    @Captor
    private ArgumentCaptor<Basket> argumentCaptor;

    private static Product product;
    private static Buyer buyer;
    private static final String email = "user@user.com";


    @BeforeEach
    void init() {
        buyer = Buyer.builder()
                .id(1L)
                .email("user@user.com")
                .name("user")
                .phone("1234567890")
                .build();

        BankRequisites bankRequisites = new BankRequisites(
                1L,
                "1111 2222 3333 4444");

        Image image = new Image(
                1L,
                "name",
                123L,
                "contentType",
                new byte[]{0x01, 0x02, 0x03});

        Vendor vendor = new Vendor(
                1L,
                "name1",
                "Name One",
                image,
                Country.RUSSIA);

        Seller seller = new Seller(
                1L,
                "NameTwo@gmail.com",
                "Name",
                " +79111111111",
                "Description seller",
                LocalDateTime.now(),
                bankRequisites,
                image);

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
                .build();
    }

    @Test
    @SneakyThrows
    void getBasketTest_whenEmpty_returnNewBasket() {
        when(buyerRepository.findByEmail(anyString())).thenReturn(Optional.of(buyer));
        when(basketRepository.findByBuyerId(1L)).thenReturn(Optional.empty());
        when(basketRepository.save(any())).thenReturn(new Basket(1L, 1L, null));

        basketService.getBasket(email);

        verify(basketRepository).save(argumentCaptor.capture());

        Basket response = argumentCaptor.getValue();

        assertEquals(1L, response.getBuyerId());
        assertNull(response.getProductsInBasket());
    }

    @Test
    @SneakyThrows
    void addProductTest_whenHaveNoProducts_returnBasketDto() {
        when(buyerRepository.findByEmail(anyString())).thenReturn(Optional.of(buyer));
        when(productRepository.findById(2L)).thenReturn(Optional.ofNullable(product));
        when(basketRepository.findByBuyerId(1L)).thenReturn(Optional.of(new Basket(1L, 1L, null)));
        when(basketRepository.save(any())).thenReturn(new Basket(1L, 1L, null));

        basketService.addProduct(email, 2L, false);

        verify(basketRepository).save(argumentCaptor.capture());

        Basket response = argumentCaptor.getValue();

        assertEquals(1L, response.getBuyerId());
        assertEquals(product.getId(), response.getProductsInBasket().get(0).getProduct().getId());
        assertEquals(1, response.getProductsInBasket().get(0).getQuantity());
        assertEquals(false, response.getProductsInBasket().get(0).getInstallation());
    }

    @Test
    @SneakyThrows
    void addProductTest_whenHaveSameProduct_returnBasketDto() {
        when(buyerRepository.findByEmail(anyString())).thenReturn(Optional.of(buyer));
        when(productRepository.findById(2L)).thenReturn(Optional.ofNullable(product));
        when(basketRepository.findByBuyerId(1L)).thenReturn(Optional.of(new Basket(
                1L, 1L, List.of(new ProductBasket(1L, product, 1, true)))));
        when(basketRepository.save(any())).thenReturn(new Basket(1L, 1L,
                List.of(new ProductBasket(1L, product, 2, true))));

        basketService.addProduct(email, 2L, true);

        verify(basketRepository).save(argumentCaptor.capture());

        Basket response = argumentCaptor.getValue();

        assertEquals(1L, response.getBuyerId());
        assertEquals(product.getId(), response.getProductsInBasket().get(0).getProduct().getId());
        assertEquals(2, response.getProductsInBasket().get(0).getQuantity());
        assertEquals(true, response.getProductsInBasket().get(0).getInstallation());
    }

    @Test
    @SneakyThrows
    void addProductTest_whenHaveSameProductWithoutInstallation_returnBasketDto() {
        List<ProductBasket> productBaskets = new ArrayList<>();
        productBaskets.add(new ProductBasket(1L, product, 1, true));

        when(buyerRepository.findByEmail(anyString())).thenReturn(Optional.of(buyer));
        when(productRepository.findById(2L)).thenReturn(Optional.ofNullable(product));
        when(basketRepository.findByBuyerId(1L)).thenReturn(Optional.of(new Basket(
                1L, 1L, productBaskets)));
        when(basketRepository.save(any())).thenReturn(new Basket(1L, 1L,
                        List.of(new ProductBasket(1L, product, 1, true))),
                new ProductBasket(2L, product, 1, false));

        basketService.addProduct(email, 2L, false);

        verify(basketRepository).save(argumentCaptor.capture());

        Basket response = argumentCaptor.getValue();

        assertEquals(1L, response.getBuyerId());
        assertEquals(2, response.getProductsInBasket().size());
        assertEquals(product.getId(), response.getProductsInBasket().get(1).getProduct().getId());
        assertEquals(1, response.getProductsInBasket().get(1).getQuantity());
        assertEquals(false, response.getProductsInBasket().get(1).getInstallation());
    }

    @Test
    @SneakyThrows
    void removeProductTest_whenHaveOneProduct_returnBasketDtoWithEmptyProductBaskets() {
        List<ProductBasket> productBaskets = new ArrayList<>();
        productBaskets.add(new ProductBasket(1L, product, 1, true));

        when(buyerRepository.findByEmail(anyString())).thenReturn(Optional.of(buyer));
        when(basketRepository.findByBuyerId(1L)).thenReturn(Optional.of(new Basket(
                1L, 1L, productBaskets)));
        when(basketRepository.save(any())).thenReturn(new Basket(1L, 1L, new ArrayList<>()));

        basketService.removeProduct(email, 2L, true);

        verify(basketRepository).save(argumentCaptor.capture());

        Basket response = argumentCaptor.getValue();

        assertEquals(1L, response.getBuyerId());
        assertNull(response.getProductsInBasket());
    }

    @Test
    @SneakyThrows
    void removeProductTest_whenHaveManyProducts_returnBasketDto() {
        List<ProductBasket> productBaskets = new ArrayList<>();
        productBaskets.add(new ProductBasket(2L, product, 2, true));

        when(buyerRepository.findByEmail(anyString())).thenReturn(Optional.of(buyer));
        when(basketRepository.findByBuyerId(1L)).thenReturn(Optional.of(new Basket(
                1L, 1L, productBaskets)));
        when(basketRepository.save(any())).thenReturn(new Basket(
                1L, 1L, List.of(new ProductBasket(2L, product, 1, true))));

        basketService.removeProduct(email, 2L, true);

        verify(basketRepository).save(argumentCaptor.capture());

        Basket response = argumentCaptor.getValue();

        assertEquals(1L, response.getBuyerId());
        assertEquals(product.getId(), response.getProductsInBasket().get(0).getProduct().getId());
        assertEquals(1, response.getProductsInBasket().get(0).getQuantity());
        assertEquals(true, response.getProductsInBasket().get(0).getInstallation());
    }
}
