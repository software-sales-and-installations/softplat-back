package ru.softplat.stats.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.softplat.stats.server.model.ReportEntry;
import ru.softplat.stats.server.model.Stats;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StatsRepository extends JpaRepository<Stats, Long> {

    @Query("SELECT new ru.softplat.stats.server.model.ReportEntry( " +
            "s.product.name, s.product.id, s.product.seller.name, " +
            "sum(s.quantity), sum(s.profit), sum(s.profitAdmin), sum(s.profitSeller)) " +
            "FROM Stats s " +
            "WHERE s.dateBuy BETWEEN :start AND :end " +
            "GROUP BY s.product.seller.id, s.product.id, s.product.name, s.product.seller.name " +
            "ORDER BY sum(s.quantity) desc ")
    List<ReportEntry> getAllStatsOrderByQuantity(
            LocalDateTime start,
            LocalDateTime end);

    @Query("SELECT new ru.softplat.stats.server.model.ReportEntry( " +
            "s.product.name, s.product.id, s.product.seller.name, " +
            "sum(s.quantity), sum(s.profit), sum(s.profitAdmin), sum(s.profitSeller)) " +
            "FROM Stats s " +
            "WHERE s.dateBuy BETWEEN :start AND :end " +
            "GROUP BY s.product.seller.id, s.product.id, s.product.name, s.product.seller.name " +
            "ORDER BY sum(s.profit) desc ")
    List<ReportEntry> getAllStatsOrderByPrice(
            LocalDateTime start,
            LocalDateTime end);

    @Query("SELECT new ru.softplat.stats.server.model.ReportEntry( " +
            "s.product.name, s.product.id, s.product.seller.name, " +
            "sum(s.quantity), sum(s.profit), sum(s.profitAdmin), sum(s.profitSeller)) " +
            "FROM Stats s " +
            "WHERE s.product.seller.id IN :sellerIds " +
            "AND s.dateBuy BETWEEN :start AND :end " +
            "GROUP BY s.product.seller.id, s.product.id, s.product.name, s.dateBuy, s.product.seller.name " +
            "ORDER BY sum(s.profit) desc ")
    List<ReportEntry> getStatsBySellerIdsOrderByPrice(
            List<Long> sellerIds,
            LocalDateTime start,
            LocalDateTime end); //TODO pageable?

    @Query("SELECT new ru.softplat.stats.server.model.ReportEntry( " +
            "s.product.name, s.product.id, s.product.seller.name, " +
            "sum(s.quantity), sum(s.profit), sum(s.profitAdmin), sum(s.profitSeller)) " +
            "FROM Stats s " +
            "WHERE s.product.seller.id IN :sellerIds " +
            "AND s.dateBuy BETWEEN :start AND :end " +
            "GROUP BY s.product.seller.id, s.product.id, s.product.name, s.dateBuy, s.product.seller.name " +
            "ORDER BY sum(s.quantity) desc ")
    List<ReportEntry> getStatsBySellerIdsOrderByQuantity(
            List<Long> sellerIds,
            LocalDateTime start,
            LocalDateTime end);
}