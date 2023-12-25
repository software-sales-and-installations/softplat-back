package ru.softplat.stats.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.softplat.stats.server.model.StatProduct;

@Repository
public interface StatProductRepository extends JpaRepository<StatProduct, Long> {
}
