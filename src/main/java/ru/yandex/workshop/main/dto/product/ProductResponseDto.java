package ru.yandex.workshop.main.dto.product;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.yandex.workshop.main.model.image.Image;
import ru.yandex.workshop.main.model.product.Category;
import ru.yandex.workshop.main.model.product.License;
import ru.yandex.workshop.main.model.product.ProductStatus;
import ru.yandex.workshop.main.model.vendor.Vendor;
import ru.yandex.workshop.security.model.user.Seller;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductResponseDto {

    Long id;

    String name;

    String description;

    String version;

    LocalDateTime productionTime;

    Image image;

    Category category;

    License license;

    Vendor vendor;

    Seller seller;

    Float price;

    Integer quantity;

    Boolean installation;

    ProductStatus productStatus;

    Boolean productAvailability;
}
