package ru.softplat.main.server.repository.seller;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;
import ru.softplat.main.server.model.seller.Seller;

import java.util.Optional;

@Repository
public interface SellerRepository extends JpaRepository<Seller, Long>,
        QuerydslPredicateExecutor<Seller> {
    Optional<Seller> findByEmail(String email);
}
