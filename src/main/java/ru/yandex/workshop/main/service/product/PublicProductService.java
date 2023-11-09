package ru.yandex.workshop.main.service.product;

import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.workshop.configuration.PageRequestOverride;
import ru.yandex.workshop.main.dto.product.ProductFilter;
import ru.yandex.workshop.main.dto.product.ProductMapper;
import ru.yandex.workshop.main.dto.product.ProductResponseDto;
import ru.yandex.workshop.main.dto.product.SortBy;
import ru.yandex.workshop.main.exception.EntityNotFoundException;
import ru.yandex.workshop.main.message.ExceptionMessage;
import ru.yandex.workshop.main.model.product.License;
import ru.yandex.workshop.main.model.product.Product;
import ru.yandex.workshop.main.model.product.ProductStatus;
import ru.yandex.workshop.main.model.product.QProduct;
import ru.yandex.workshop.main.model.vendor.Country;
import ru.yandex.workshop.main.repository.product.ProductRepository;
import ru.yandex.workshop.main.repository.seller.SellerRepository;
import ru.yandex.workshop.main.util.QPredicates;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class PublicProductService {

    private final ProductRepository productRepository;
    private final SellerRepository sellerRepository;

    public List<ProductResponseDto> getProductsOfSeller(Long sellerId, int from, int size) {
        sellerRepository.findById(sellerId)
                .orElseThrow(() -> new EntityNotFoundException(ExceptionMessage.ENTITY_NOT_FOUND_EXCEPTION.label));
        PageRequestOverride pageRequest = PageRequestOverride.of(from, size);
        // TODO у меня в Postman валится этот метод, Павел Михайлов, проверь пожалуйста
        return productRepository.findProductBySellerId(sellerId, pageRequest)
                .stream()
                .map(ProductMapper.INSTANCE::productToProductResponseDto)
                .collect(Collectors.toList());
    }

    public ProductResponseDto getProductById(Long productId) {
        return ProductMapper.INSTANCE.productToProductResponseDto(getProductFromDatabase(productId));
    }

    private Product getProductFromDatabase(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException(ExceptionMessage.ENTITY_NOT_FOUND_EXCEPTION.label));
    }

    public List<ProductResponseDto> getProductsByFilter(ProductFilter productFilter, int from, int size, SortBy sort) {
        Sort sortBy = (sort.equals(SortBy.NEWEST)) ?
                Sort.by("productionTime").descending() : Sort.by("price").ascending();

        PageRequest pageRequest = PageRequestOverride.of(from, size, sortBy);

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

        if (productFilter.getIsDemo() != null) {
            BooleanExpression licenseExpression;
            if (productFilter.getIsDemo()) {
                licenseExpression = product.license.eq(License.DEMO);
            } else {
                licenseExpression = product.license.ne(License.DEMO);
            }
            predicate = ExpressionUtils.and(predicate, licenseExpression);
        }

        Page<Product> products = productRepository.findAll(predicate, pageRequest);

        return products.getContent().stream()
                .map(ProductMapper.INSTANCE::productToProductResponseDto)
                .collect(Collectors.toList());
    }
}