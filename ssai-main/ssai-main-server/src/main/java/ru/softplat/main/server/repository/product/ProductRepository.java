package ru.softplat.main.server.repository.product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;
import ru.server.configuration.PageRequestOverride;
import ru.softplat.main.server.model.product.Product;
import ru.softplat.main.server.model.product.ProductStatus;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>,
        QuerydslPredicateExecutor<Product> {

    List<Product> findAllByProductStatusOrderByProductionTimeDesc(ProductStatus productStatus, PageRequestOverride pageRequest);
}
