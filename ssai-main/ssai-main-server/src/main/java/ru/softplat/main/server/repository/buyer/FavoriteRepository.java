package ru.softplat.main.server.repository.buyer;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.softplat.main.server.model.buyer.Favorite;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    List<Favorite> findAllByBuyerId(Long buyerId);

    boolean existsByBuyerIdAndProductId(Long buyerId, Long productId);

    Optional<Favorite> findByBuyerIdAndProductId(Long buyerId, Long productId);

    @Query("SELECT f.product.id " +
            "FROM Favorite f " +
            "WHERE f.buyer.id IN ( " +
            "   SELECT f1.buyer.id " +
            "   FROM Favorite f1 " +
            "   WHERE f1.buyer.id != :userId " +
            "   AND f1.product.id IN ( " +
            "       SELECT f2.product.id " +
            "       FROM Favorite f2 " +
            "       WHERE f2.buyer.id = :userId) ) " +
            "AND f.product.id NOT IN ( " +
            "   SELECT f3.product.id " +
            "   FROM Favorite f3 " +
            "   WHERE f3.buyer.id = :userId) " +
            "GROUP BY f.product.id, f.product.productionTime " +
            "ORDER BY count (f.product.id) DESC, " +
            "f.product.productionTime DESC ")
    List<Long> getRecommendations(Long userId, Pageable pageable);
}

