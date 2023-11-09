package ru.yandex.workshop.main.repository.buyer;

import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.yandex.workshop.main.model.buyer.ProductOrder;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ProductOrderRepository extends JpaRepository<ProductOrder, Long>,
        QuerydslPredicateExecutor<ProductOrder> {

//    List<ProductOrder> findAllProductOrderAndTimestampBetween(
//            Predicate predicate,
//            PageRequest pageRequest,
//            @Param("start") LocalDate start,
//            @Param("end") LocalDate end);

//    List<ProductOrder> findAllByProductOrderByDateBetween(Predicate predicate, PageRequest pageRequest, LocalDate start, LocalDate end);
}
