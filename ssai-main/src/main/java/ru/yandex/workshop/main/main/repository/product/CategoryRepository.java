package ru.yandex.workshop.main.main.repository.product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.yandex.workshop.main.main.model.product.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
}
