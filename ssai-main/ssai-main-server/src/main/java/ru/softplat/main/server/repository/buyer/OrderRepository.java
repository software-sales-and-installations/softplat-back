package ru.softplat.main.server.repository.buyer;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.softplat.main.server.model.buyer.Order;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByBuyer_Id(Long buyerId, Pageable pageRequest);

    @Query("SELECT o FROM Order o " +
            "JOIN OrderPosition op " +
            "ON o.id = op.orderId " +
            "WHERE o.buyer.id = :buyerId " +
            "AND op.product = :productId")
    Optional<Order> findOrderByBuyerIdAndProductId(@Param("buyerId") Long buyerId, @Param("productId") Long productId);
}
