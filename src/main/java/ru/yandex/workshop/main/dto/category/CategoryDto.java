package ru.yandex.workshop.main.dto.category;


import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategoryDto {
    @NotBlank(message = "Необходимо указать название категории")
    @Pattern(regexp = "^[a-zA-Zа-яА-Я]{2,20}$", message = "Длина навзания категории должна быть от 2 до 20 символов. Цифры в названии не допускаются.")
    String name;
}
