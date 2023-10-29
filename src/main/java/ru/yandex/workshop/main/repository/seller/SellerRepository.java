package ru.yandex.workshop.main.repository.seller;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.yandex.workshop.main.model.seller.Seller;

@Repository
public interface SellerRepository extends JpaRepository<Seller, Long> {
}
