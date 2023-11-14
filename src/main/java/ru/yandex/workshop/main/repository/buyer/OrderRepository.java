package ru.yandex.workshop.main.repository.buyer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.yandex.workshop.configuration.PageRequestOverride;
import ru.yandex.workshop.main.model.buyer.Order;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByBuyer_Id(Long buyerId, PageRequestOverride pageRequestOverride);
}
