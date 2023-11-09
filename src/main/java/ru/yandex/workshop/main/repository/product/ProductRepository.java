package ru.yandex.workshop.main.repository.product;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.yandex.workshop.configuration.PageRequestOverride;
import ru.yandex.workshop.main.model.product.License;
import ru.yandex.workshop.main.model.product.Product;
import ru.yandex.workshop.main.model.product.ProductStatus;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findProductBySellerId(Long sellerId, PageRequestOverride pageRequest);

    List<Product> findAllByProductStatusOrderByProductionTimeDesc(ProductStatus productStatus, PageRequestOverride pageRequest);

    Optional<Product> findProductByNameEqualsAndLicenseEquals(String name, License license);

    @Query(value = "select p.* " +
            "from product as p " +
            "left join image as i on p.image_id = i.id " +
            "left join seller as s on p.seller_id = s.id " +
            "left join vendor as v on p.vendor_id = v.id " +
            "left join category as c on p.category_id = c.id " +
            "where p.license like 'LICENSE' " +
            "and p.license like 'FREE' " +
            "and p.status like 'PUBLISHED' " +
            "and p.availability is true "+
            "order by p.production_time desc", nativeQuery = true)
    List<Product> findAllProducts(Pageable pageable);
}
