package ru.softplat.stats.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.softplat.stats.server.model.StatSeller;

@Repository
public interface StatSellerRepository extends JpaRepository<StatSeller, Long> {
}
