package ru.softplat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;
import ru.yandex.workshop.stats.dto.SellerReportEntry;
import ru.yandex.workshop.stats.model.Stats;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StatsRepository extends JpaRepository<Stats, Long>,
        QuerydslPredicateExecutor<Stats> {

    @Query("SELECT new ru.yandex.workshop.stats.dto.SellerReportEntry( " +
            "s.product.name, sum (s.quantity), sum(s.amount)) " +
            "FROM Stats s " +
            "WHERE s.dateBuy BETWEEN :start AND :end " +
            "GROUP BY s.product.seller.id, s.product.id, s.product.name ")
    List<SellerReportEntry> getAllStats(
            LocalDateTime start,
            LocalDateTime end);

    @Query("SELECT new ru.yandex.workshop.stats.dto.SellerReportEntry( " +
            "s.product.name, sum (s.quantity), sum(s.amount)) " +
            "FROM Stats s " +
            "WHERE s.product.seller.id IN :sellerId " +
            "AND s.dateBuy BETWEEN :start AND :end " +
            "GROUP BY s.product.seller.id, s.product.id, s.product.name ")
    List<SellerReportEntry> getStatsByProduct(
            List<Long> sellerId,
            LocalDateTime start,
            LocalDateTime end);

    @Query("SELECT new ru.yandex.workshop.stats.dto.SellerReportEntry( " +
            "s.product.name, sum (s.quantity), sum(s.amount)) " +
            "FROM Stats s " +
            "WHERE s.product.seller.email IN :email " +
            "AND s.dateBuy BETWEEN :start AND :end " +
            "GROUP BY s.product.seller.id, s.product.id, s.product.name ")
    List<SellerReportEntry> getAllStatsEmailSeller(
            String email,
            LocalDateTime start,
            LocalDateTime end);
}

