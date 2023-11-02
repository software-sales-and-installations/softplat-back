package ru.yandex.workshop.main.repository.product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.yandex.workshop.main.model.product.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
}
