package ru.softplat.main.server.repository.buyer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.softplat.main.server.model.buyer.BasketPosition;

@Repository
public interface BasketPositionRepository extends JpaRepository<BasketPosition, Long> {
    BasketPosition findAllByBasketIdAndProduct_IdAndInstallation(Long basketId, Long productId, Boolean installation);
}
