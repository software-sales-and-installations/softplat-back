package ru.softplat.service.product;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.softplat.configuration.PageRequestOverride;
import ru.softplat.dto.product.ProductsSearchRequestDto;
import ru.softplat.dto.product.SortBy;
import ru.softplat.exception.EntityNotFoundException;
import ru.softplat.message.ExceptionMessage;
import ru.softplat.model.product.Product;
import ru.softplat.model.product.ProductStatus;
import ru.softplat.model.product.QProduct;
import ru.softplat.repository.product.ProductRepository;
import ru.softplat.util.QPredicates;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class SearchProductService {

    private final ProductRepository productRepository;

    @Transactional(readOnly = true)
    public Product getProductById(Long productId) {
        return productRepository.findById(productId).orElseThrow(
                () -> new EntityNotFoundException(
                        ExceptionMessage.ENTITY_NOT_FOUND_EXCEPTION.getMessage(String.valueOf(productId), Product.class)
                ));
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

        Predicate predicate;
        Page<Product> products;

        if (productsSearchRequestDto != null) {
            predicate = QPredicates.builder()
                    .add(statusPublishedExpression)
                    .add(productsSearchRequestDto.getText(), textPredicateFunction)
                    .add(productsSearchRequestDto.getSellerIds(), product.seller.id::in)
                    .add(productsSearchRequestDto.getVendorIds(), product.vendor.id::in)
                    .add(productsSearchRequestDto.getCategories(), product.category.id::in)
                    .add(productsSearchRequestDto.getPriceMin(), product.price::goe)
                    .add(productsSearchRequestDto.getPriceMax(), product.price::loe)
                    .add(productsSearchRequestDto.getCountries(), product.vendor.country::in)
                    .add(productsSearchRequestDto.getLicenses(), product.license::in)
                    .buildAnd();

            products = productRepository.findAll(predicate, pageRequest);
        } else {
            products = productRepository.findAll(pageRequest);
        }

        return new ArrayList<>(products.getContent());
    }
}