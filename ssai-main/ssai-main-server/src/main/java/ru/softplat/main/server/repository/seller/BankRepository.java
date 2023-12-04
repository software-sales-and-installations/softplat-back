package ru.softplat.main.server.repository.seller;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;
import ru.softplat.main.server.model.seller.BankRequisites;

@Repository
public interface BankRepository extends JpaRepository<BankRequisites, Long>,
        QuerydslPredicateExecutor<BankRequisites> {
}
