package ru.softplat.stats.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.softplat.stats.server.model.StatDemo;

import java.util.Optional;

@Repository
public interface StatDemoRepository extends JpaRepository<StatDemo, Long> {
    Optional<StatDemo> findByBuyerNameAndProductNameAndProductSellerName(String buyerName, String productName,
                                                                         String productSellerName);

    int countAllByProductName(String productName);
}
