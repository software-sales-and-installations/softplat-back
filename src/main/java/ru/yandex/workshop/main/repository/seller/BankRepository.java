package ru.yandex.workshop.main.repository.seller;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.yandex.workshop.main.model.seller.BankRequisites;

@Repository
public interface BankRepository extends JpaRepository<BankRequisites, Long> {
}
