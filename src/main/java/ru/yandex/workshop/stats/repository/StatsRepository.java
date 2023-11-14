package ru.yandex.workshop.stats.repository;

import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;
import ru.yandex.workshop.stats.model.Stats;

import java.util.List;

@Repository
public interface StatsRepository extends JpaRepository<Stats, Long>,
        QuerydslPredicateExecutor<Stats> {

    List<Stats> findAll(Predicate predicate);
}