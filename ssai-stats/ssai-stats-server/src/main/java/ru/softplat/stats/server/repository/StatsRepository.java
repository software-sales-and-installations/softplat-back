package ru.softplat.stats.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.softplat.stats.server.dto.SellerReportEntry;
import ru.softplat.stats.server.model.Stats;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StatsRepository extends JpaRepository<Stats, Long> {

    @Query("SELECT new ru.softplat.stats.server.dto.SellerReportEntry( " +
            "s.product.name, sum (s.quantity), sum(s.amount)) " +
            "FROM Stats s " +
            "WHERE s.dateBuy BETWEEN :start AND :end " +
            "GROUP BY s.product.seller.id, s.product.id, s.product.name ")
    List<SellerReportEntry> getAllStats(
            LocalDateTime start,
            LocalDateTime end);

    @Query("SELECT new ru.softplat.stats.server.dto.SellerReportEntry( " +
            "s.product.name, sum (s.quantity), sum(s.amount)) " +
            "FROM Stats s " +
            "WHERE s.product.seller.id IN :sellerId " +
            "AND s.dateBuy BETWEEN :start AND :end " +
            "GROUP BY s.product.seller.id, s.product.id, s.product.name ")
    List<SellerReportEntry> getStatsByProduct(
            List<Long> sellerId,
            LocalDateTime start,
            LocalDateTime end);
}
