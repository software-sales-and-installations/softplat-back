package ru.yandex.workshop.main.service.product;

import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.SneakyThrows;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import ru.yandex.workshop.configuration.PageRequestOverride;
import ru.yandex.workshop.main.dto.product.ProductDto;
import ru.yandex.workshop.main.dto.product.ProductFilter;
import ru.yandex.workshop.main.dto.product.ProductMapper;
import ru.yandex.workshop.main.dto.user.mapper.SellerMapper;
import ru.yandex.workshop.main.model.product.License;
import ru.yandex.workshop.main.model.product.Product;
import ru.yandex.workshop.main.model.product.ProductStatus;
import ru.yandex.workshop.main.model.product.QProduct;
import ru.yandex.workshop.main.model.seller.Seller;
import ru.yandex.workshop.main.model.vendor.Country;
import ru.yandex.workshop.main.repository.product.ProductRepository;
import ru.yandex.workshop.main.repository.seller.SellerRepository;
import ru.yandex.workshop.main.util.QPredicates;
import ru.yandex.workshop.security.dto.UserDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class PublicProductServiceTest {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private SellerRepository sellerRepository;
    private Product savedProduct1, savedProduct2, savedProduct3;

    @BeforeEach
    void setUp() {
        UserDto sellerDto1 = UserDto.builder()
                .name("seller1")
                .email("seller1@email.ru")
                .phone("1111111111").build();

        UserDto sellerDto2 = UserDto.builder()
                .name("seller2")
                .email("seller2@email.ru")
                .phone("2222222222")
                .build();

        UserDto sellerDto3 = UserDto.builder()
                .name("seller3")
                .email("seller3@email.ru")
                .phone("3333333333")
                .build();

        Seller seller1 = SellerMapper.INSTANCE.userDtoToSeller(sellerDto1);
        Seller seller2 = SellerMapper.INSTANCE.userDtoToSeller(sellerDto2);
        Seller seller3 = SellerMapper.INSTANCE.userDtoToSeller(sellerDto3);

        seller1.setRegistrationTime(LocalDateTime.now());
        seller2.setRegistrationTime(LocalDateTime.now());
        seller3.setRegistrationTime(LocalDateTime.now());

        Seller savedSeller1 = sellerRepository.save(seller1);
        Seller savedSeller2 = sellerRepository.save(seller2);
        Seller savedSeller3 = sellerRepository.save(seller3);

        long categoryId1 = 1, vendorId1 = 1;
        ProductDto productDto1 = ProductDto.builder()
                .name("product1")
                .description("product1 description")
                .version("2.0.0.1")
                .category(categoryId1)
                .license(License.LICENSE)
                .vendor(vendorId1)
                .price(1000F)
                .quantity(5)
                .installation(true)
                .productAvailability(true)
                .installationPrice(10.00F)
                .build();

        long categoryId2 = 2, vendorId2 = 2;
        ProductDto productDto2 = ProductDto.builder()
                .name("product2")
                .description("product2 description")
                .version("2.0.0.1")
                .category(categoryId2)
                .license(License.LICENSE)
                .vendor(vendorId2)
                .price(2000F)
                .quantity(5)
                .installation(true)
                .productAvailability(true)
                .installationPrice(10.00F)
                .build();

        long categoryId3 = 2, vendorId3 = 3;
        ProductDto productDto3 = ProductDto.builder()
                .name("product3")
                .description("product3 description and details")
                .version("2.0.0.1")
                .category(categoryId3)
                .license(License.LICENSE)
                .vendor(vendorId3)
                .price(500F)
                .quantity(5)
                .installation(true)
                .productAvailability(true)
                .installationPrice(10.00F)
                .build();

        Product product1 = ProductMapper.INSTANCE.productDtoToProduct(productDto1);
        Product product2 = ProductMapper.INSTANCE.productDtoToProduct(productDto2);
        Product product3 = ProductMapper.INSTANCE.productDtoToProduct(productDto3);

        product1.setProductStatus(ProductStatus.PUBLISHED);
        product2.setProductStatus(ProductStatus.PUBLISHED);
        product3.setProductStatus(ProductStatus.PUBLISHED);

        product3.setProductionTime(LocalDateTime.of(2022, 10, 1, 12, 40));
        product2.setProductionTime(LocalDateTime.of(2022, 11, 1, 12, 40));
        product1.setProductionTime(LocalDateTime.of(2022, 12, 1, 12, 40));

        product1.setSeller(seller1);
        product2.setSeller(seller2);
        product3.setSeller(seller3);

        savedProduct1 = productRepository.save(product1);
        savedProduct2 = productRepository.save(product2);
        savedProduct3 = productRepository.save(product3);
    }

    @Test
    @SneakyThrows
    void whenSearchCategories1And2_thenReturnAllProducts() {
        ProductFilter productFilter = new ProductFilter();
        productFilter.setCategories(List.of(1L, 2L));

        Predicate predicate = getPredicate(productFilter);
        Sort sortBy = Sort.by("productionTime").descending();
        PageRequest pageRequest = PageRequestOverride.of(0, 10, sortBy);

        List<Product> actual = productRepository.findAll(predicate, pageRequest).toList();
        List<Product> expect = List.of(savedProduct1, savedProduct2, savedProduct3);

        assertEquals(expect.size(), actual.size());
        assertEquals(expect.get(0).getName(), actual.get(0).getName());
        assertEquals(expect.get(1).getName(), actual.get(1).getName());
        assertEquals(expect.get(2).getName(), actual.get(2).getName());
    }

    @Test
    @SneakyThrows
    void whenSearchVendorIds1And2_thenReturnProducts1And2() {
        ProductFilter productFilter = new ProductFilter();
        productFilter.setVendorIds(List.of(1L, 2L));

        Predicate predicate = getPredicate(productFilter);
        Sort sortBy = Sort.by("productionTime").descending();
        PageRequest pageRequest = PageRequestOverride.of(0, 10, sortBy);

        List<Product> actual = productRepository.findAll(predicate, pageRequest).toList();
        List<Product> expect = List.of(savedProduct1, savedProduct2);

        assertEquals(expect.size(), actual.size());
        assertEquals(expect.get(0).getName(), actual.get(0).getName());
        assertEquals(expect.get(1).getName(), actual.get(1).getName());
    }

    @Test
    @SneakyThrows
    void whenSearchTextDescriptionAndDetails_thenReturnProduct3() {
        ProductFilter productFilter = new ProductFilter();
        productFilter.setText("description and details");

        Predicate predicate = getPredicate(productFilter);
        Sort sortBy = Sort.by("productionTime").descending();
        PageRequest pageRequest = PageRequestOverride.of(0, 10, sortBy);

        List<Product> actual = productRepository.findAll(predicate, pageRequest).toList();
        List<Product> expect = List.of(savedProduct3);

        assertEquals(expect.size(), actual.size());
        assertEquals(expect.get(0).getName(), actual.get(0).getName());
    }

    @Test
    @SneakyThrows
    void whenSearchCategoryId2AndVendorId2_thenReturnProduct2() {
        ProductFilter productFilter = new ProductFilter();
        productFilter.setCategories(List.of(2L));
        productFilter.setVendorIds(List.of(2L));

        Predicate predicate = getPredicate(productFilter);
        Sort sortBy = Sort.by("productionTime").descending();
        PageRequest pageRequest = PageRequestOverride.of(0, 10, sortBy);

        List<Product> actual = productRepository.findAll(predicate, pageRequest).toList();
        List<Product> expect = List.of(savedProduct2);

        assertEquals(expect.size(), actual.size());
        assertEquals(expect.get(0).getName(), actual.get(0).getName());
    }

//    @Test
//    @SneakyThrows
//    void whenSearchSellerId1AndSellerId3AndTextProduct_thenReturnProducts1And3() {
//        ProductFilter productFilter = new ProductFilter();
//        productFilter.setSellerIds(List.of(1L, 3L));
//        productFilter.setText("pRoDucT");
//
//        Predicate predicate = getPredicate(productFilter);
//        Sort sortBy = Sort.by("productionTime").descending();
//        PageRequest pageRequest = PageRequestOverride.of(0, 10, sortBy);
//
//        List<Product> actual = productRepository.findAll(predicate, pageRequest).toList();
//        List<Product> expect = List.of(savedProduct1, savedProduct3);
//
//        assertEquals(expect.size(), actual.size());
//        assertEquals(expect.get(0).getName(), actual.get(0).getName());
//        assertEquals(expect.get(1).getName(), actual.get(1).getName());
//    }

    @Test
    @SneakyThrows
    void whenSearchCountryIsRussian_thenReturnProduct3() {
        ProductFilter productFilter = new ProductFilter();
        productFilter.setIsRussian(true);

        Predicate predicate = getPredicate(productFilter);
        Sort sortBy = Sort.by("productionTime").descending();
        PageRequest pageRequest = PageRequestOverride.of(0, 10, sortBy);

        List<Product> actual = productRepository.findAll(predicate, pageRequest).toList();
        List<Product> expect = List.of(savedProduct3);

        assertEquals(expect.size(), actual.size());
        assertEquals(expect.get(0).getName(), actual.get(0).getName());
    }

    @Test
    @SneakyThrows
    void whenSearchCountryIsNotRussian_thenReturnProducts1And2() {
        ProductFilter productFilter = new ProductFilter();
        productFilter.setIsRussian(false);

        Predicate predicate = getPredicate(productFilter);
        Sort sortBy = Sort.by("productionTime").descending();
        PageRequest pageRequest = PageRequestOverride.of(0, 10, sortBy);

        List<Product> actual = productRepository.findAll(predicate, pageRequest).toList();
        List<Product> expect = List.of(savedProduct1, savedProduct2);

        assertEquals(expect.size(), actual.size());
        assertEquals(expect.get(0).getName(), actual.get(0).getName());
        assertEquals(expect.get(1).getName(), actual.get(1).getName());
    }

    @Test
    @SneakyThrows
    void whenSearchWithEmptyProductFilter_thenReturnAllProducts() {
        ProductFilter productFilter = new ProductFilter();

        Predicate predicate = getPredicate(productFilter);
        Sort sortBy = Sort.by("productionTime").descending();
        PageRequest pageRequest = PageRequestOverride.of(0, 10, sortBy);

        List<Product> actual = productRepository.findAll(predicate, pageRequest).toList();
        List<Product> expect = List.of(savedProduct1, savedProduct2, savedProduct3);

        assertEquals(expect.size(), actual.size());
        assertEquals(expect.get(0).getName(), actual.get(0).getName());
        assertEquals(expect.get(1).getName(), actual.get(1).getName());
        assertEquals(expect.get(2).getName(), actual.get(2).getName());
    }

    @Test
    @SneakyThrows
    void whenSearchProductsMatchingNoCriteria_thenReturnEmptyList() {
        ProductFilter productFilter = new ProductFilter();
        productFilter.setText("foo");
        productFilter.setSellerIds(List.of(3L));
        productFilter.setVendorIds(List.of(4L));
        productFilter.setCategories(List.of(5L));
        productFilter.setIsRussian(true);

        Predicate predicate = getPredicate(productFilter);
        Sort sortBy = Sort.by("productionTime").descending();
        PageRequest pageRequest = PageRequestOverride.of(0, 10, sortBy);

        List<Product> actual = productRepository.findAll(predicate, pageRequest).toList();
        List<Product> expect = Lists.emptyList();

        assertEquals(actual, expect);
    }

    @Test
    @SneakyThrows
    void whenSearchSortingByPrice_thenReturnProductsFromLowerToHigherPrice() {
        ProductFilter productFilter = new ProductFilter();

        Predicate predicate = getPredicate(productFilter);
        Sort sortBy = Sort.by("price").ascending();
        PageRequest pageRequest = PageRequestOverride.of(0, 10, sortBy);

        List<Product> actual = productRepository.findAll(predicate, pageRequest).toList();
        List<Product> expect = List.of(savedProduct3, savedProduct1, savedProduct2);

        assertEquals(expect.size(), actual.size());
        assertEquals(expect.get(0).getName(), actual.get(0).getName());
        assertEquals(expect.get(1).getName(), actual.get(1).getName());
        assertEquals(expect.get(2).getName(), actual.get(2).getName());
    }

    @Test
    @SneakyThrows
    void whenSearchPriceFrom1000To2000_thenReturnProducts1And2() {
        ProductFilter productFilter = new ProductFilter();
        productFilter.setPriceMin(1000F);
        productFilter.setPriceMax(2000F);

        Predicate predicate = getPredicate(productFilter);
        Sort sortBy = Sort.by("price").ascending();
        PageRequest pageRequest = PageRequestOverride.of(0, 10, sortBy);

        List<Product> actual = productRepository.findAll(predicate, pageRequest).toList();
        List<Product> expect = List.of(savedProduct1, savedProduct2);

        assertEquals(expect.size(), actual.size());
        assertEquals(expect.get(0).getName(), actual.get(0).getName());
        assertEquals(expect.get(1).getName(), actual.get(1).getName());
    }

    @Test
    @SneakyThrows
    void whenSearchPriceLowerThan450_thenReturnEmptyList() {
        ProductFilter productFilter = new ProductFilter();
        productFilter.setPriceMax(450F);

        Predicate predicate = getPredicate(productFilter);
        Sort sortBy = Sort.by("productionTime").descending();
        PageRequest pageRequest = PageRequestOverride.of(0, 10, sortBy);

        List<Product> actual = productRepository.findAll(predicate, pageRequest).toList();
        List<Product> expect = Lists.emptyList();

        assertEquals(actual, expect);
    }

    @Test
    @SneakyThrows
    void whenSearchGreaterThanOrEqualTo2000_thenReturnProduct2() {
        ProductFilter productFilter = new ProductFilter();
        productFilter.setPriceMin(2000F);

        Predicate predicate = getPredicate(productFilter);
        Sort sortBy = Sort.by("productionTime").descending();
        PageRequest pageRequest = PageRequestOverride.of(0, 10, sortBy);

        List<Product> actual = productRepository.findAll(predicate, pageRequest).toList();
        List<Product> expect = List.of(savedProduct2);

        assertEquals(actual.get(0).getName(), expect.get(0).getName());
    }

    private Predicate getPredicate(ProductFilter productFilter) {
        QProduct product = QProduct.product;
        BooleanExpression statusPublishedExpression = product.productStatus.eq(ProductStatus.PUBLISHED);

        Function<String, Predicate> textPredicateFunction = text -> {
            BooleanExpression nameExpression = product.name.toLowerCase().like("%" + text.toLowerCase() + "%");
            BooleanExpression descriptionExpression = product.description.toLowerCase().like("%" + text.toLowerCase() + "%");
            return nameExpression.or(descriptionExpression);
        };

        Predicate predicate = QPredicates.builder()
                .add(statusPublishedExpression)
                .add(productFilter.getText(), textPredicateFunction)
                .add(productFilter.getSellerIds(), product.seller.id::in)
                .add(productFilter.getVendorIds(), product.vendor.id::in)
                .add(productFilter.getCategories(), product.category.id::in)
                .add(productFilter.getPriceMin(), product.price::goe)
                .add(productFilter.getPriceMax(), product.price::loe)
                .buildAnd();

        if (productFilter.getIsRussian() != null) {
            BooleanExpression countryExpression;
            if (productFilter.getIsRussian()) {
                countryExpression = product.vendor.country.eq(Country.RUSSIA);
            } else {
                countryExpression = product.vendor.country.ne(Country.RUSSIA);
            }
            predicate = ExpressionUtils.and(predicate, countryExpression);
        }
        return predicate;
    }
}