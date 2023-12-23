package ru.softplat.main.server.repository.buyer;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.softplat.main.server.model.buyer.Order;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByBuyerId(Long buyerId, Pageable pageRequest);
}
