package ru.softplat.stats.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.softplat.stats.server.model.StatBuyer;

@Repository
public interface StatBuyerRepository extends JpaRepository<StatBuyer, Long> {
}
