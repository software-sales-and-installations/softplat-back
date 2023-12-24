package ru.softplat.main.server.repository.product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;
import ru.softplat.main.dto.product.ProductStatus;
import ru.softplat.main.server.model.product.Product;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>,
        QuerydslPredicateExecutor<Product> {

    Page<Product> findAllByProductStatusOrderByProductionTimeDesc(ProductStatus productStatus,
                                                                  Pageable pageRequest);

    List<Product> findAllByProductStatusAndSellerIdOrderByProductionTimeDesc(ProductStatus productStatus,
                                                                  Long sellerId, Pageable pageRequest);

    long countAllByProductStatus(ProductStatus status);

    long countAllByProductStatusAndSellerId(ProductStatus status, long sellerId);
}
