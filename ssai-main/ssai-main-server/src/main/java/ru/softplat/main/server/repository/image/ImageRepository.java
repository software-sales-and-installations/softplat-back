package ru.softplat.main.server.repository.image;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;
import ru.softplat.main.server.model.image.Image;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long>,
        QuerydslPredicateExecutor<Image> {
}
