package ru.softplat.main.server.repository.buyer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.softplat.main.server.model.buyer.Buyer;

import java.util.Optional;

@Repository
public interface BuyerRepository extends JpaRepository<Buyer, Long> {
    Optional<Buyer> findByEmail(String email);

    boolean existsByEmail(String email);
}
