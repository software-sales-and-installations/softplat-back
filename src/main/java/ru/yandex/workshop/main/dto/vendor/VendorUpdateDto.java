package ru.yandex.workshop.main.dto.vendor;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.yandex.workshop.main.model.vendor.Country;

import javax.validation.constraints.Pattern;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VendorUpdateDto {
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[а-я])(?=.*[А-Я]).{0,20}$", message = "Длина навзания компании должна быть от 2 до 20 символов.")
    String name;
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[а-я])(?=.*[А-Я]).{0,500}$", message = "Длина описания должна быть от 2 до 500 символов.")
    String description;
    Country country;
}
