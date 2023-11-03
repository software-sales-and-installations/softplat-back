package ru.yandex.workshop.main.dto.product;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;
import ru.yandex.workshop.main.model.image.Image;
import ru.yandex.workshop.main.model.product.Category;
import ru.yandex.workshop.main.model.product.License;
import ru.yandex.workshop.main.model.product.ProductStatus;
import ru.yandex.workshop.main.model.seller.Seller;
import ru.yandex.workshop.main.model.vendor.Vendor;

import javax.validation.constraints.*;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductDto {

    Long id;

    @NotBlank(message = "Необходимо указать имя продукта")
    @Pattern(message = "Неверные символы в названии товара",
            regexp = "^[а-яА-Яa-zA-Z0-9\\s№-]+$")
    @Size(min = 2, max = 255, message = "Длина названия продукта должна быть от 2 до 255 символов")
    String name;

    @NotBlank(message = "Необходимо указать описание продукта")
    @Size(min = 2, max = 1024, message = "Длина описания продукта должна быть от 2 до 1024 символов")
    String description;

    @NotBlank(message = "Необходимо указать версию продукта")
    @Size(min = 2, max = 30, message = "Длина наименования версии продукта должна быть от 2 до 30 символов")
    String version;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss", iso = DateTimeFormat.ISO.DATE_TIME)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @PastOrPresent
    LocalDateTime productionTime;

    Image image;

    Category category;

    License license;

    Vendor vendor;

    Seller seller;

    @PositiveOrZero
    Float price;

    @PositiveOrZero
    Integer quantity;

    Boolean installation;

    ProductStatus productStatus;

    Boolean productAvailability;
}
