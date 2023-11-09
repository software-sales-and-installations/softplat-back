package ru.yandex.workshop.main.repository.buyer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;
import ru.yandex.workshop.main.model.buyer.Buyer;

import java.util.Optional;

@Repository
public interface BuyerRepository extends JpaRepository<Buyer, Long>, QuerydslPredicateExecutor<Buyer> {
    Optional<Buyer> findByEmail(String email);
}
