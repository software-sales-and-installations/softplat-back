package ru.yandex.workshop.main.web.controller.image;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.workshop.main.service.image.ImageService;

@RestController
@RequestMapping(path = "/image")
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;

    @Operation(summary = "Получение изображения по id", description = "Доступ для всех")
    @GetMapping("/{id}")
    public ResponseEntity<byte[]> getImage(@Parameter(description = "Идентификатор уникален для всех изображений на сайте")
                                               @PathVariable Long id) {
        return imageService.getImageAsByteArray(id);
    }
}
