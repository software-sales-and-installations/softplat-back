package ru.yandex.workshop.main.repository.vendor;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.yandex.workshop.main.model.vendor.Country;

@Repository
public interface CountryRepository extends JpaRepository<Country, Long> {
}
