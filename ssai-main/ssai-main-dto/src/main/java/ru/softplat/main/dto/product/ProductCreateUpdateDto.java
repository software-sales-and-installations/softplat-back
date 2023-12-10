package ru.softplat.main.dto.product;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.softplat.main.dto.validation.New;
import ru.softplat.main.dto.validation.SixCharFloat;
import ru.softplat.main.dto.validation.Update;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.PositiveOrZero;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductCreateUpdateDto {
    @NotBlank(groups = {New.class}, message = "Необходимо указать имя продукта")
    @Pattern(groups = {New.class, Update.class}, regexp = "^[a-zA-Zа-яА-Я-.,\\s]{2,255}$", message = "Неверные символы в названии товара. " +
            "Длина названия продукта должна быть от 2 до 255 символов. Цифры в названии не допускаются.")
    String name;

    @NotBlank(groups = {New.class}, message = "Необходимо указать описание продукта")
    @Pattern(groups = {New.class, Update.class}, regexp = "[0-9a-zA-Zа-яА-Я-@#$.,?%^&+=!\\s]{2,500}$",
            message = "Длина описания должна быть от 2 до 500 символов.")
    String description;

    @NotBlank(groups = {New.class}, message = "Необходимо указать версию продукта")
    @Pattern(groups = {New.class, Update.class}, regexp = "^[0-9a-z-.,/]{2,30}$",
            message = "Длина версии должна быть от 2 до 30 символов.")
    String version;

    @NotNull(groups = {New.class}, message = "Необходимо указать категорию продукта.")
    Long category;

    Boolean hasDemo;

    @NotNull(groups = {New.class}, message = "Необходимо указать вендора продукта.")
    Long vendor;

    @PositiveOrZero(groups = {New.class, Update.class})
    @SixCharFloat(groups = {New.class, Update.class})
    @NotNull(groups = {New.class}, message = "Необходимо указать цену продукта.")
    Float price;

    @PositiveOrZero(groups = {New.class, Update.class})
    @NotNull(groups = {New.class}, message = "Необходимо указать количество продукта.")
    Integer quantity;

    Boolean installation;

    @PositiveOrZero(groups = {New.class, Update.class})
    @SixCharFloat(groups = {New.class, Update.class})
    @Schema(description = "Обязательно должна быть при создании, если есть возможность купить с установкой")
    Float installationPrice;
}