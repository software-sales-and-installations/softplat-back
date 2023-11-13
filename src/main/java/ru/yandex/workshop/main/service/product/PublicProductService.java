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
import ru.yandex.workshop.main.dto.product.ProductsSearchRequestDto;
import ru.yandex.workshop.main.dto.product.SortBy;
import ru.yandex.workshop.main.exception.EntityNotFoundException;
import ru.yandex.workshop.main.message.ExceptionMessage;
import ru.yandex.workshop.main.model.product.License;
import ru.yandex.workshop.main.model.product.Product;
import ru.yandex.workshop.main.model.product.ProductStatus;
import ru.yandex.workshop.main.model.product.QProduct;
import ru.yandex.workshop.main.model.vendor.Country;
import ru.yandex.workshop.main.repository.product.ProductRepository;
import ru.yandex.workshop.main.util.QPredicates;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class PublicProductService {

    private final ProductRepository productRepository;

    @Transactional(readOnly = true)
    public Product getProductById(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException(ExceptionMessage.ENTITY_NOT_FOUND_EXCEPTION.label));
    }

    @Transactional(readOnly = true)
    public List<Product> getProductsByFilter(ProductsSearchRequestDto productsSearchRequestDto, int from, int size, SortBy sort) {
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
                .add(productsSearchRequestDto.getText(), textPredicateFunction)
                .add(productsSearchRequestDto.getSellerIds(), product.seller.id::in)
                .add(productsSearchRequestDto.getVendorIds(), product.vendor.id::in)
                .add(productsSearchRequestDto.getCategories(), product.category.id::in)
                .add(productsSearchRequestDto.getPriceMin(), product.price::goe)
                .add(productsSearchRequestDto.getPriceMax(), product.price::loe)
                .buildAnd();

        if (productsSearchRequestDto.getIsRussian() != null) {
            BooleanExpression countryExpression;
            if (productsSearchRequestDto.getIsRussian()) {
                countryExpression = product.vendor.country.eq(Country.RUSSIA);
            } else {
                countryExpression = product.vendor.country.ne(Country.RUSSIA);
            }
            predicate = ExpressionUtils.and(predicate, countryExpression);
        }

        if (productsSearchRequestDto.getIsDemo() != null) {
            BooleanExpression licenseExpression;
            if (productsSearchRequestDto.getIsDemo()) {
                licenseExpression = product.license.eq(License.DEMO);
            } else {
                licenseExpression = product.license.ne(License.DEMO);
            }
            predicate = ExpressionUtils.and(predicate, licenseExpression);
        }

        Page<Product> products = productRepository.findAll(predicate, pageRequest);

        return new ArrayList<>(products.getContent());
    }
}