package ru.yandex.workshop.main.service.product;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.workshop.configuration.PageRequestOverride;
import ru.yandex.workshop.main.dto.product.ProductDto;
import ru.yandex.workshop.main.dto.product.ProductMapper;
import ru.yandex.workshop.main.dto.product.ProductResponseDto;
import ru.yandex.workshop.main.model.product.Category;
import ru.yandex.workshop.main.model.product.License;
import ru.yandex.workshop.main.model.product.Product;
import ru.yandex.workshop.main.model.product.ProductStatus;
import ru.yandex.workshop.main.model.seller.BankRequisites;
import ru.yandex.workshop.main.model.vendor.Country;
import ru.yandex.workshop.main.model.vendor.Vendor;
import ru.yandex.workshop.main.repository.product.CategoryRepository;
import ru.yandex.workshop.main.repository.product.ProductRepository;
import ru.yandex.workshop.main.repository.vendor.VendorRepository;
import ru.yandex.workshop.main.service.image.ImageService;
import ru.yandex.workshop.security.model.user.Seller;
import ru.yandex.workshop.security.repository.SellerRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Transactional
class ProductServiceTest {

    private PublicProductService publicProductService;
    private UserProductService userProductService;
    private ProductRepository productRepository;
    private SellerRepository sellerRepository;
    private CategoryRepository categoryRepository;
    private VendorRepository vendorRepository;
    private ImageService imageService;
    private PageRequestOverride pageRequest;
    static Product product;
    static Long productId;
    static ProductResponseDto productResponseDto;
    static ProductDto productDto;
    static ProductDto productForUpdate;
    static Vendor vendor;
    static Long vendorId;
    static Seller seller;
    static Long sellerId;
    static Category category;
    static Long categoryId;
    static BankRequisites bankRequisites;
    static LocalDateTime time;
    static String formattedString;

    @BeforeEach
    void beforeEach() {
        productRepository = mock(ProductRepository.class);
        sellerRepository = mock(SellerRepository.class);
        categoryRepository = mock(CategoryRepository.class);
        vendorRepository = mock(VendorRepository.class);
        userProductService = new UserProductService(
                productRepository,
                sellerRepository,
                imageService);
        publicProductService = new PublicProductService(
                productRepository,
                sellerRepository);

        pageRequest = PageRequestOverride.of(0, 20);

        time = LocalDateTime.of(LocalDate.of(2023, 11, 1),
                LocalTime.of(22, 21, 41, 760048200));
        DateTimeFormatter aFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        formattedString = time.format(aFormatter);

        bankRequisites = new BankRequisites(
                1L,
                "1111 2222 3333 4444");

        vendorId = 1L;
        vendor = Vendor.builder()
                .id(vendorId)
                .name("name1")
                .description("Name One")
                .country(Country.RUSSIA)
                .build();

        sellerId = 1L;
        seller = Seller.builder()
                .id(sellerId)
                .email("NameTwo@gmail.com")
                .name("Name")
                .phone(" +79111111111")
                .description("Description seller")
                .registrationTime(time)
                .requisites(bankRequisites)
                .build();

        categoryId = 1L;
        category = new Category(
                categoryId,
                "Category");

        productId = 1L;
        product = Product.builder()
                .id(productId)
                .description("Description product")
                .version("2.0.0.1")
                .productionTime(time)
                .category(category)
                .license(License.LICENSE)
                .vendor(vendor)
                .seller(seller)
                .price(1000.421F)
                .quantity(5)
                .installation(true)
                .productStatus(ProductStatus.DRAFT)
                .productAvailability(true)
                .installationPrice(10.00F)
                .build();

        productDto = ProductDto.builder()
                .description("Description product")
                .version("2.0.0.1")
                .category(categoryId)
                .license(License.LICENSE)
                .vendor(vendorId)
                .seller(sellerId)
                .price(1000.421F)
                .quantity(5)
                .installation(true)
                .productAvailability(true)
                .installationPrice(10.00F)
                .build();

        productForUpdate = ProductDto.builder()
                .description("New description product")
                .version("2.0.0.5")
                .category(categoryId)
                .license(License.LICENSE)
                .vendor(vendorId)
                .seller(sellerId)
                .price(1000.421F)
                .quantity(5)
                .installation(true)
                .productAvailability(true)
                .installationPrice(10.00F)
                .build();

        productResponseDto = ProductMapper.INSTANCE.productToProductResponseDto(product);

        when(categoryRepository.save(category))
                .then(invocation -> invocation.getArgument(0));
        when(vendorRepository.save(vendor))
                .then(invocation -> invocation.getArgument(0));
        when(sellerRepository.save(seller))
                .then(invocation -> invocation.getArgument(0));
        when(productRepository.save(any()))
                .then(invocation -> invocation.getArgument(0));
    }

    @Test
    @DisplayName("Вызов метода getProductsSellerTest: получение всех продуктов конкретного seller")
    void getProductsSellerTest() {
        when(sellerRepository
                .findById(
                        product.getSeller().getId()))
                .thenReturn(Optional.of(product.getSeller()));
        when(productRepository
                .findProductBySellerId(
                        product.getSeller().getId(),
                        pageRequest))
                .thenReturn(Collections.singletonList(product));
        final List<ProductResponseDto> productDtoList = publicProductService
                .getProductsOfSeller(
                        product.getSeller().getId(),
                        0,
                        20);
        assertNotNull(productDtoList);
        assertEquals(1, productDtoList.size());
        assertEquals(product.getName(), productDtoList.get(0).getName());
    }

    @Test
    @DisplayName("Вызов метода getProductByIdTest: получение продукта по id")
    void getProductByIdTest() {
        when(sellerRepository
                .findById(
                        product.getSeller().getId()))
                .thenReturn(Optional.of(product.getSeller()));
        when(productRepository
                .findById(
                        product.getId()))
                .thenReturn(Optional.of(product));
        var productDtoGet = publicProductService.getProductById(product.getId());
        assertNotNull(productDtoGet);
        assertEquals(product.getName(), productDtoGet.getName());
        assertEquals(product.getDescription(), productDtoGet.getDescription());
        assertEquals(product.getVersion(), productDtoGet.getVersion());
        assertEquals(product.getCategory().getId(), productDtoGet.getCategory().getId());
        assertEquals(product.getVendor().getId(), productDtoGet.getVendor().getId());
        assertEquals(product.getPrice(), productDtoGet.getPrice());
        assertEquals(product.getQuantity(), productDtoGet.getQuantity());
        assertEquals(product.getPrice(), productDtoGet.getPrice());
        assertEquals(product.getInstallationPrice(), productDtoGet.getInstallationPrice());
    }

    @Test
    @DisplayName("Вызов метода createProductTest: создание продукта")
    void createProductTest() {
        when(categoryRepository
                .findById(category.getId()))
                .thenReturn(Optional.of(category));
        when(sellerRepository
                .findByEmail(seller.getEmail()))
                .thenReturn(Optional.of(seller));
        when(vendorRepository
                .findById(vendor.getId()))
                .thenReturn(Optional.of(vendor));
        when(productRepository
                .save(product))
                .thenReturn(product);
        var productDtoSave = userProductService.createProduct(seller.getEmail(), productDto);
        assertNotNull(productDtoSave);
        assertEquals(product.getName(), productDtoSave.getName());
        assertEquals(product.getDescription(), productDtoSave.getDescription());
        assertEquals(product.getVersion(), productDtoSave.getVersion());
        assertEquals(product.getCategory().getId(), productDtoSave.getCategory().getId());
        assertEquals(product.getVendor().getId(), productDtoSave.getVendor().getId());
        assertEquals(product.getPrice(), productDtoSave.getPrice());
        assertEquals(product.getQuantity(), productDtoSave.getQuantity());
        assertEquals(product.getPrice(), productDtoSave.getPrice());
        assertEquals(product.getInstallationPrice(), productDtoSave.getInstallationPrice());
    }

    @Test
    @DisplayName("Вызов метода updateProductTest: обновление продукта")
    void updateProductTest() {
        when(sellerRepository
                .findByEmail(seller.getEmail()))
                .thenReturn(Optional.of(seller));
        when(productRepository
                .findById(product.getId()))
                .thenReturn(Optional.of(product));

        product.setName(productForUpdate.getName());
        product.setDescription(productForUpdate.getDescription());
        product.setVersion(productForUpdate.getVersion());
        product.setPrice(productForUpdate.getPrice());
        product.setQuantity(productForUpdate.getQuantity());
        product.setInstallation(productForUpdate.getInstallation());
        product.setProductAvailability(productForUpdate.getProductAvailability());

        var productDtoUpdate = userProductService.updateProduct(seller.getEmail(), product.getId(),
                productForUpdate);
        assertNotNull(productDtoUpdate);
        assertEquals(product.getName(), productDtoUpdate.getName());
        assertEquals(product.getDescription(), productDtoUpdate.getDescription());
        assertEquals(product.getVersion(), productDtoUpdate.getVersion());
        assertEquals(product.getCategory().getId(), productDtoUpdate.getCategory().getId());
        assertEquals(product.getVendor().getId(), productDtoUpdate.getVendor().getId());
        assertEquals(product.getPrice(), productDtoUpdate.getPrice());
        assertEquals(product.getQuantity(), productDtoUpdate.getQuantity());
        assertEquals(product.getPrice(), productDtoUpdate.getPrice());
        assertEquals(product.getInstallationPrice(), productDtoUpdate.getInstallationPrice());
    }

    @Test
    @DisplayName("Вызов метода updateStatusProductOnSentTest: обновление статуса на 'SHIPPED'")
    void updateStatusProductOnSentTest() {
        when(sellerRepository
                .findByEmail(seller.getEmail()))
                .thenReturn(Optional.of(seller));

        when(productRepository
                .findById(product.getId()))
                .thenReturn(Optional.of(product));

        product.setProductStatus(ProductStatus.SHIPPED);

        var productDtoUpdate = userProductService.updateStatusProductOnSent(seller.getEmail(),
                product.getId());
        assertNotNull(product);
        assertEquals(product.getProductStatus(), productDtoUpdate.getProductStatus());
    }

    /*@Test
    @DisplayName("Вызов метода getAllProductsSellerTest: получение всех продуктов всех seller")
    void getAllProductsSellerTest() {
        when(sellerRepository
                .findById(
                        product.getSeller().getId()))
                .thenReturn(Optional.of(product.getSeller()));

        when(productRepository.findAll(pageRequest))
                .thenReturn(Collections.singletonList(product));
        final List<ProductResponseDto> productDtoList = productService
                .getAllProductsSeller(
                        0,
                        20);
        assertNotNull(productDtoList);
        assertEquals(1, productDtoList.size());
        assertEquals(product.getName(), productDtoList.get(0).getName());
    }*/

    @Test
    @DisplayName("Вызов метода updateStatusProductOnPublishedTest: обновление статуса на 'PUBLISHED'")
    void updateStatusProductOnPublishedTest() {
        when(sellerRepository
                .findByEmail(seller.getEmail()))
                .thenReturn(Optional.of(seller));

        when(productRepository
                .findById(product.getId()))
                .thenReturn(Optional.of(product));

        product.setProductStatus(ProductStatus.PUBLISHED);

        var productDtoUpdate = userProductService.updateStatusProductOnSent(seller.getEmail(),
                product.getId());
        assertNotNull(product);
        assertEquals(product.getProductStatus(), productDtoUpdate.getProductStatus());
    }

    @Test
    @DisplayName("Вызов метода updateStatusProductOnRejectedTest: обновление статуса на 'REJECTED'")
    void updateStatusProductOnRejectedTest() {
        when(sellerRepository
                .findByEmail(seller.getEmail()))
                .thenReturn(Optional.of(seller));

        when(productRepository
                .findById(product.getId()))
                .thenReturn(Optional.of(product));

        product.setProductStatus(ProductStatus.REJECTED);

        var productDtoUpdate = userProductService.updateStatusProductOnSent(seller.getEmail(),
                product.getId());
        assertNotNull(product);
        assertEquals(product.getProductStatus(), productDtoUpdate.getProductStatus());
    }

    @Test
    @DisplayName("Вызов метода deleteProductTest: удаление продукта")
    void deleteProductAdminTest() {
        when(sellerRepository
                .findById(seller.getId()))
                .thenReturn(Optional.of(seller));
        when(productRepository
                .findById(product.getId()))
                .thenReturn(Optional.of(product));

        userProductService.deleteProduct(product.getId());
    }
}