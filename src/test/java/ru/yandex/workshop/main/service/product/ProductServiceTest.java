package ru.yandex.workshop.main.service.product;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.workshop.main.config.PageRequestOverride;
import ru.yandex.workshop.main.dto.product.ProductDto;
import ru.yandex.workshop.main.dto.product.ProductForUpdate;
import ru.yandex.workshop.main.dto.product.ProductMapper;
import ru.yandex.workshop.main.dto.product.ProductResponseDto;
import ru.yandex.workshop.main.model.image.Image;
import ru.yandex.workshop.main.model.product.Category;
import ru.yandex.workshop.main.model.product.License;
import ru.yandex.workshop.main.model.product.Product;
import ru.yandex.workshop.main.model.product.ProductStatus;
import ru.yandex.workshop.main.model.seller.BankRequisites;
import ru.yandex.workshop.main.model.seller.Seller;
import ru.yandex.workshop.main.model.vendor.Country;
import ru.yandex.workshop.main.model.vendor.Vendor;
import ru.yandex.workshop.main.repository.product.CategoryRepository;
import ru.yandex.workshop.main.repository.product.ProductRepository;
import ru.yandex.workshop.main.repository.seller.SellerRepository;
import ru.yandex.workshop.main.repository.vendor.VendorRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static ru.yandex.workshop.main.model.product.ProductStatus.DRAFT;

@Transactional
class ProductServiceTest {

    private ProductService productService;
    private ProductRepository productRepository;
    private SellerRepository sellerRepository;
    private CategoryRepository categoryRepository;
    private VendorRepository vendorRepository;
    private PageRequestOverride pageRequest;
    static Product product;
    static ProductDto productDto;
    static ProductResponseDto productResponseDto;
    static ProductForUpdate productForUpdate;
    static Vendor vendor;
    static Seller seller;
    static Image image;
    static Category category;
    static BankRequisites bankRequisites;
    static LocalDateTime time;
    static String foramttedString;

    @BeforeEach
    void beforeEach() {
        productRepository = mock(ProductRepository.class);
        sellerRepository = mock(SellerRepository.class);
        categoryRepository = mock(CategoryRepository.class);
        vendorRepository = mock(VendorRepository.class);
        productService = new ProductService(
                productRepository,
                sellerRepository,
                categoryRepository,
                vendorRepository);

        pageRequest = PageRequestOverride.of(0, 20);

        time = LocalDateTime.of(LocalDate.of(2023, 11, 1),
                LocalTime.of(22, 21, 41, 760048200));
        DateTimeFormatter aFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        foramttedString = time.format(aFormatter);

        bankRequisites = new BankRequisites(
                1L,
                "1111 2222 3333 4444");

        image = new Image(
                1L,
                "name",
                123.12F,
                "contentType",
                new byte[]{0x01, 0x02, 0x03});

        vendor = new Vendor(
                1L,
                "name1",
                "Name One",
                1L,
                Country.RUSSIA);

        seller = new Seller(
                1L,
                "NameTwo@gmail.com",
                "Name",
                " +79111111111",
                "Description seller",
                time,
                bankRequisites,
                image);

        category = new Category(
                1L,
                "Category");

        product = new Product(
                1L,
                "Name product",
                "Description product",
                "2.0.0.1",
                time,
                image,
                category,
                License.LICENSE,
                vendor,
                seller,
                1000.421F,
                5,
                true,
                DRAFT,
                true,
                10.00F);

        productForUpdate = new ProductForUpdate(
                "Name product 2",
                "Description product 2",
                "2.0.0.5",
                time,
                image,
                category,
                License.LICENSE,
                vendor,
                seller,
                10.421F,
                0,
                false,
                DRAFT,
                false,
                10.00F);

        productDto = ProductMapper.INSTANCE.productToProductDto(product);
        productResponseDto = ProductMapper.INSTANCE.productToProductResponseDto(product);

        when(categoryRepository.save(category))
                .then(invocation -> invocation.getArgument(0));
        when(vendorRepository.save(vendor))
                .then(invocation -> invocation.getArgument(0));
        when(sellerRepository.save(seller))
                .then(invocation -> invocation.getArgument(0));
        when(productRepository.save(product))
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
        final List<ProductResponseDto> productDtoList = productService
                .getProductsSeller(
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
        var productDtoGet = productService.getProductById(seller.getId(), product.getId());
        assertNotNull(productDtoGet);
        assertEquals(product.getName(), productDtoGet.getName());
        assertEquals(product.getDescription(), productDtoGet.getDescription());
        assertEquals(product.getVersion(), productDtoGet.getVersion());
        assertEquals(product.getCategory(), productDtoGet.getCategory());
        assertEquals(product.getVendor(), productDtoGet.getVendor());
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
                .findById(seller.getId()))
                .thenReturn(Optional.of(seller));
        when(vendorRepository
                .findById(vendor.getId()))
                .thenReturn(Optional.of(vendor));
        when(productRepository
                .save(product))
                .thenReturn(product);
        ProductResponseDto productDtoSave = productService.createProduct(ProductMapper.INSTANCE.productToProductDto(product));
        assertNotNull(productDtoSave);
        assertEquals(product.getName(), productDtoSave.getName());
        assertEquals(product.getDescription(), productDtoSave.getDescription());
        assertEquals(product.getVersion(), productDtoSave.getVersion());
        assertEquals(product.getCategory(), productDtoSave.getCategory());
        assertEquals(product.getVendor(), productDtoSave.getVendor());
        assertEquals(product.getPrice(), productDtoSave.getPrice());
        assertEquals(product.getQuantity(), productDtoSave.getQuantity());
        assertEquals(product.getPrice(), productDtoSave.getPrice());
        assertEquals(product.getInstallationPrice(), productDtoSave.getInstallationPrice());
    }

    @Test
    @DisplayName("Вызов метода updateProductTest: обновление продукта")
    void updateProductTest() {
        when(sellerRepository
                .findById(seller.getId()))
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

        var productDtoUpdate = productService.updateProduct(
                seller.getId(),
                product.getId(),
                productForUpdate);
        assertNotNull(productDtoUpdate);
        assertEquals(product.getName(), productDtoUpdate.getName());
        assertEquals(product.getDescription(), productDtoUpdate.getDescription());
        assertEquals(product.getVersion(), productDtoUpdate.getVersion());
        assertEquals(product.getCategory(), productDtoUpdate.getCategory());
        assertEquals(product.getVendor(), productDtoUpdate.getVendor());
        assertEquals(product.getPrice(), productDtoUpdate.getPrice());
        assertEquals(product.getQuantity(), productDtoUpdate.getQuantity());
        assertEquals(product.getPrice(), productDtoUpdate.getPrice());
        assertEquals(product.getInstallationPrice(), productDtoUpdate.getInstallationPrice());
    }

    @Test
    @DisplayName("Вызов метода updateStatusProductOnSentTest: обновление статуса на 'SHIPPED'")
    void updateStatusProductOnSentTest() {
        when(sellerRepository
                .findById(seller.getId()))
                .thenReturn(Optional.of(seller));

        when(productRepository
                .findById(product.getId()))
                .thenReturn(Optional.of(product));

        product.setProductStatus(ProductStatus.SHIPPED);

        var productDtoUpdate = productService.updateStatusProductOnSent(seller.getId(),
                product.getId());
        assertNotNull(product);
        assertEquals(product.getProductStatus(), productDtoUpdate.getProductStatus());
    }

    @Test
    @DisplayName("Вызов метода getAllProductsSellerTest: получение всех продуктов всех seller")
    void getAllProductsSellerTest() {
        when(sellerRepository
                .findById(
                        product.getSeller().getId()))
                .thenReturn(Optional.of(product.getSeller()));

        when(productRepository.findAllBy(pageRequest))
                .thenReturn(Collections.singletonList(product));
        final List<ProductResponseDto> productDtoList = productService
                .getAllProductsSeller(
                        0,
                        20);
        assertNotNull(productDtoList);
        assertEquals(1, productDtoList.size());
        assertEquals(product.getName(), productDtoList.get(0).getName());
    }

    @Test
    @DisplayName("Вызов метода getProductByIdAdminTest: получение продуктa по id")
    void getProductByIdAdminTest() {
        when(sellerRepository
                .findById(
                        product.getSeller().getId()))
                .thenReturn(Optional.of(product.getSeller()));

        when(productRepository.findById(product.getId()))
                .thenReturn(Optional.of(product));
        var productByIdAdmin = productService
                .getProductByIdAdmin(product.getId());
        assertNotNull(productByIdAdmin);
        assertEquals(product.getName(), productByIdAdmin.getName());
        assertEquals(product.getDescription(), productByIdAdmin.getDescription());
        assertEquals(product.getVersion(), productByIdAdmin.getVersion());
        assertEquals(product.getCategory(), productByIdAdmin.getCategory());
        assertEquals(product.getVendor(), productByIdAdmin.getVendor());
        assertEquals(product.getPrice(), productByIdAdmin.getPrice());
        assertEquals(product.getQuantity(), productByIdAdmin.getQuantity());
        assertEquals(product.getPrice(), productByIdAdmin.getPrice());
        assertEquals(product.getInstallationPrice(), productByIdAdmin.getInstallationPrice());
    }

    @Test
    @DisplayName("Вызов метода updateStatusProductOnPublishedTest: обновление статуса на 'PUBLISHED'")
    void updateStatusProductOnPublishedTest() {
        when(sellerRepository
                .findById(seller.getId()))
                .thenReturn(Optional.of(seller));

        when(productRepository
                .findById(product.getId()))
                .thenReturn(Optional.of(product));

        product.setProductStatus(ProductStatus.PUBLISHED);

        var productDtoUpdate = productService.updateStatusProductOnSent(seller.getId(),
                product.getId());
        assertNotNull(product);
        assertEquals(product.getProductStatus(), productDtoUpdate.getProductStatus());
    }

    @Test
    @DisplayName("Вызов метода updateStatusProductOnRejectedTest: обновление статуса на 'REJECTED'")
    void updateStatusProductOnRejectedTest() {
        when(sellerRepository
                .findById(seller.getId()))
                .thenReturn(Optional.of(seller));

        when(productRepository
                .findById(product.getId()))
                .thenReturn(Optional.of(product));

        product.setProductStatus(ProductStatus.REJECTED);

        var productDtoUpdate = productService.updateStatusProductOnSent(seller.getId(),
                product.getId());
        assertNotNull(product);
        assertEquals(product.getProductStatus(), productDtoUpdate.getProductStatus());
    }

    @Test
    @DisplayName("Вызов метода deleteProductAdminTest: удаление продукта")
    void deleteProductAdminTest() {
        when(sellerRepository
                .findById(seller.getId()))
                .thenReturn(Optional.of(seller));
        when(productRepository
                .findById(product.getId()))
                .thenReturn(Optional.of(product));

        productService.deleteProductAdmin(product.getId());
    }
}
