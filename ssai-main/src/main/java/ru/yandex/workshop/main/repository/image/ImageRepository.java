package ru.yandex.workshop.main.repository.image;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.yandex.workshop.main.model.image.Image;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
}
