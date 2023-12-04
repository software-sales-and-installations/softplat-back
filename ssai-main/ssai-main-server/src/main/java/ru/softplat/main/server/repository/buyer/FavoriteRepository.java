package ru.softplat.main.server.repository.buyer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.softplat.main.server.model.buyer.Favorite;

import java.util.List;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    List<Favorite> findAllByBuyerId(Long buyerId);

    boolean existsByBuyerIdAndProductId(Long buyerId, Long productId);
}

