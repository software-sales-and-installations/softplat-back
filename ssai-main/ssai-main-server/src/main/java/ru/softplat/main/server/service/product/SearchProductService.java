package ru.softplat.main.server.service.product;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.softplat.main.dto.product.ProductStatus;
import ru.softplat.main.dto.product.ProductsSearchRequestDto;
import ru.softplat.main.dto.product.SortBy;
import ru.softplat.main.server.configuration.PageRequestOverride;
import ru.softplat.main.server.exception.EntityNotFoundException;
import ru.softplat.main.server.message.ExceptionMessage;
import ru.softplat.main.server.model.product.Product;
import ru.softplat.main.server.model.product.QProduct;
import ru.softplat.main.server.repository.product.ProductRepository;
import ru.softplat.main.server.util.QPredicates;

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
                        ExceptionMessage.ENTITY_NOT_FOUND_EXCEPTION.getMessage(productId, Product.class)
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
                    .buildAnd();

            products = productRepository.findAll(predicate, pageRequest);
        } else {
            products = productRepository.findAll(pageRequest);
        }

        return new ArrayList<>(products.getContent());
    }

    @Transactional(readOnly = true)
    public List<Product> getSimilarProducts(long productId, int from, int size) {
        Sort sortBy = Sort.by("productionTime").descending();
        PageRequest pageRequest = PageRequestOverride.of(from, size, sortBy);

        Product product = getProductById(productId);
        QProduct qProduct = QProduct.product;
        BooleanExpression statusPublishedExpression = qProduct.productStatus.eq(ProductStatus.PUBLISHED);

        Predicate predicate = QPredicates.builder()
                .add(statusPublishedExpression)
                .add(productId, qProduct.id::ne)
                .add(QPredicates.builder()
                        .add(product.getVendor().getId(), qProduct.vendor.id::eq)
                        .add(product.getCategory().getId(), qProduct.category.id::eq)
                        .buildOr())
                .buildAnd();

        Page<Product> products = productRepository.findAll(predicate, pageRequest);
        return new ArrayList<>(products.getContent());
    }

    @Transactional(readOnly = true)
    public List<Product> getProductsByIds(List<Long> productIds) {
        return productRepository.findAllById(productIds);
    }

    @Transactional(readOnly = true)
    public long getTotalProductsCount(ProductStatus status) {
        if (status == null)
            return productRepository.count();
        return productRepository.countAllByProductStatus(status);
    }
}