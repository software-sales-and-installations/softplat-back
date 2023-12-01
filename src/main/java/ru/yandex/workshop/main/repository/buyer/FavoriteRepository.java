package ru.yandex.workshop.main.repository.buyer;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.yandex.workshop.main.model.buyer.Favorite;

import java.util.List;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    List<Favorite> findAllByBuyerEmail(String buyerEmail);

    boolean existsByBuyerEmailAndProductId(String buyerEmail, Long productId);

    @Query("SELECT f.product.id " +
            "FROM Favorite f " +
            "WHERE f.buyer.id IN ( " +
            "   SELECT f1.buyer.id " +
            "   FROM Favorite f1 " +
            "   WHERE f1.buyer.email != :email " +
            "   AND f1.product.id IN ( " +
            "       SELECT f2.product.id " +
            "       FROM Favorite f2 " +
            "       WHERE f2.buyer.email = :email) ) " +
            "AND f.product.id NOT IN ( " +
            "   SELECT f3.product.id " +
            "   FROM Favorite f3 " +
            "   WHERE f3.buyer.email = :email) " +
            "GROUP BY f.product.id " +
            "ORDER BY count (f.product.id) DESC, " +
            "f.product.productionTime DESC ")
    List<Long> getRecommendations(String email, Pageable pageable);
}

