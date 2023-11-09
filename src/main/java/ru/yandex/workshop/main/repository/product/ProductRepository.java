package ru.yandex.workshop.main.repository.product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.yandex.workshop.configuration.PageRequestOverride;
import ru.yandex.workshop.main.model.product.License;
import ru.yandex.workshop.main.model.product.Product;
import ru.yandex.workshop.main.model.product.ProductStatus;

import java.util.List;
import java.util.Optional;

import static ru.yandex.workshop.main.model.product.License.DEMO;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findProductBySellerId(Long sellerId, PageRequestOverride pageRequest);

    List<Product> findAllByProductStatusOrderByProductionTimeDesc(ProductStatus productStatus, PageRequestOverride pageRequest);

    Optional<Product> findProductByNameEqualsAndLicenseEquals(String name, License license);
}
