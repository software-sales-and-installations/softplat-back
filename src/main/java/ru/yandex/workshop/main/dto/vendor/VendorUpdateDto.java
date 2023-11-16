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
    @Pattern(regexp = "^[0-9a-zA-Zа-яА-Я\\s]{2,255}$", message = "Длина названия компании должна быть от 2 до 20 символов.")
    String name;
    @Pattern(regexp = "^[0-9a-zA-Zа-яА-Я-@#$,.%^&+=!\\s]{2,500}$", message = "Длина описания должна быть от 2 до 500 символов.")
    String description;
    Country country;
}
