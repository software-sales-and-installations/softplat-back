package ru.yandex.workshop.main.repository.buyer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.yandex.workshop.main.model.buyer.Buyer;

@Repository
public interface BuyerRepository extends JpaRepository<Buyer, Long> {
    boolean existsBuyerByEmail(String email);
    boolean existsBuyerById(Long id);
}
