package ru.yandex.workshop.main.dto.vendor;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.Length;
import ru.yandex.workshop.main.model.vendor.Country;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VendorDto {
    @NotBlank(message = "Необходимо указать имя")
    @Length(min = 2, max = 20, message = "Длина имени должна быть от 2 до 20 символов")
    String name;
    @NotBlank(message = "Необходимо указать описание")
    @Length(min = 2, max = 500, message = "Длина имени должна быть от 2 до 500 символов")
    String description;
    @NotNull
    Country country;
}