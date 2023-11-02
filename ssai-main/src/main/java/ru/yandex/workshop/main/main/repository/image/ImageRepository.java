package ru.yandex.workshop.main.main.repository.image;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.yandex.workshop.main.main.model.image.Image;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
}
