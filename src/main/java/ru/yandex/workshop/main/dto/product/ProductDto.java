package ru.yandex.workshop.main.dto.product;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;
import ru.yandex.workshop.main.dto.validation.New;
import ru.yandex.workshop.main.model.product.License;

import javax.validation.constraints.*;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductDto {
    @NotBlank(groups = {New.class}, message = "Необходимо указать имя продукта")
    @Pattern(message = "Неверные символы в названии товара",
            regexp = "^[а-яА-Яa-zA-Z0-9\\s№-]+$")
    @Size(min = 2, max = 255, message = "Длина названия продукта должна быть от 2 до 255 символов")
    String name;

    @NotBlank(groups = {New.class}, message = "Необходимо указать описание продукта")
    @Size(min = 2, max = 1024, message = "Длина описания продукта должна быть от 2 до 1024 символов")
    String description;

    @NotBlank(groups = {New.class}, message = "Необходимо указать версию продукта")
    @Size(min = 2, max = 30, message = "Длина наименования версии продукта должна быть от 2 до 30 символов")
    String version;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss", iso = DateTimeFormat.ISO.DATE_TIME)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @PastOrPresent
    LocalDateTime productionTime;

    @NotBlank(groups = {New.class}, message = "Необходимо указать категорию продукта.")
    Long category;

    @NotBlank(groups = {New.class}, message = "Необходимо указать тип лицензии.")
    License license;

    @NotBlank(groups = {New.class}, message = "Необходимо указать вендора продукта.")
    Long vendor;

    @NotBlank(groups = {New.class}, message = "Необходимо указать продавца продукта.")
    Long seller;

    @PositiveOrZero
    @NotBlank(groups = {New.class}, message = "Необходимо указать цену продукта.")
    Float price;

    @PositiveOrZero
    @NotBlank(groups = {New.class}, message = "Необходимо указать количество продукта.")
    Integer quantity;

    Boolean installation;

    Boolean productAvailability;
}
