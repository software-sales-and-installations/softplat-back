package ru.yandex.workshop.main.repository.product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.yandex.workshop.main.config.PageRequestOverride;
import ru.yandex.workshop.main.model.product.Product;
import ru.yandex.workshop.main.model.product.ProductStatus;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findProductBySellerId(Long sellerId, PageRequestOverride pageRequest);

    List<Product> findAllBy(PageRequestOverride pageRequest);

    List<Product> findAllByProductStatus(ProductStatus productStatus, PageRequestOverride pageRequest);

}
