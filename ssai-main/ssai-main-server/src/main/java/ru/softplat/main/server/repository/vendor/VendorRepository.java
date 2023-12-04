package ru.softplat.main.server.repository.vendor;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;
import ru.softplat.main.server.model.vendor.Vendor;

@Repository
public interface VendorRepository extends JpaRepository<Vendor, Long>,
        QuerydslPredicateExecutor<Vendor> {
}
