package ru.yandex.workshop.main.repository.buyer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.yandex.workshop.main.model.buyer.OrderPosition;

@Repository
public interface OrderPositionRepository extends JpaRepository<OrderPosition, Long> {
}
