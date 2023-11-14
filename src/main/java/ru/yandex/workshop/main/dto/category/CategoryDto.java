package ru.yandex.workshop.main.dto.category;


import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategoryDto {
    @NotBlank(message = "Необходимо указать название категории")
    @Size(min = 2, max = 20, message = "Длина названия должна быть от 2 до 20 символов")
    String name;
}
