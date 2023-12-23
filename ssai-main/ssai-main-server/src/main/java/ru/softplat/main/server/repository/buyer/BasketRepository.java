package ru.softplat.main.server.repository.buyer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.softplat.main.server.model.buyer.Basket;

import java.util.Optional;

@Repository
public interface BasketRepository extends JpaRepository<Basket, Long> {
    Optional<Basket> findByBuyerId(Long buyerId);
}
