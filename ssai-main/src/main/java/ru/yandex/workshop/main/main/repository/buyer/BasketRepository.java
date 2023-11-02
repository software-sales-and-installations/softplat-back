package ru.yandex.workshop.main.main.repository.buyer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.yandex.workshop.main.main.model.buyer.Basket;

@Repository
public interface BasketRepository extends JpaRepository<Basket, Long> {
}
