package ru.yandex.workshop.main.repository.buyer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;
import ru.yandex.workshop.main.model.buyer.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long>, QuerydslPredicateExecutor<Order> {
}
