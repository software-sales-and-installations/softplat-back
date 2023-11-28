package ru.softplat.repository.buyer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.softplat.model.buyer.Favorite;

import java.util.List;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    List<Favorite> findAllByBuyerEmail(String buyerEmail);

    boolean existsByBuyerEmailAndProductId(String buyerEmail, Long productId);
}

