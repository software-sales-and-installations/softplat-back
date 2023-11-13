package ru.yandex.workshop.stats.repository;

import com.querydsl.core.types.Predicate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;
import ru.yandex.workshop.stats.model.SellerReportEntry;
import ru.yandex.workshop.stats.model.Stats;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface StatsRepository extends JpaRepository<Stats, Long>,
        QuerydslPredicateExecutor<Stats> {
    List<Stats> findAll(Predicate predicate);

    @Query(value =
            //"SELECT s.* " +
                    "FROM Stats as s " +
                    "WHERE s.dateBuy BETWEEN ?1 AND ?2 "/* +
                    "GROUP BY s.product.seller.name"*/)
    List<Stats> findSellerReport(LocalDate start, LocalDate end, Predicate predicate);
}
