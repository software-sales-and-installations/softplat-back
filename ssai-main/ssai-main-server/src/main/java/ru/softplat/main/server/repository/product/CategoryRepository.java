package ru.softplat.main.server.repository.product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;
import ru.softplat.main.server.model.product.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long>,
        QuerydslPredicateExecutor<Category> {
}
