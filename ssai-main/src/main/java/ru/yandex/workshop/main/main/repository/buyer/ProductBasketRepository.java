package ru.yandex.workshop.main.main.repository.buyer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.yandex.workshop.main.main.model.buyer.ProductBasket;

@Repository
public interface ProductBasketRepository extends JpaRepository<ProductBasket, Long> {
}
