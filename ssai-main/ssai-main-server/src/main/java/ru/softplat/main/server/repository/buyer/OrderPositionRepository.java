package ru.softplat.main.server.repository.buyer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.softplat.main.server.model.buyer.OrderPosition;

@Repository
public interface OrderPositionRepository extends JpaRepository<OrderPosition, Long> {
}
