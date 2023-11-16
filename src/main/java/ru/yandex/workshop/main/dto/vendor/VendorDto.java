package ru.yandex.workshop.main.dto.vendor;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.yandex.workshop.main.model.vendor.Country;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VendorDto {
    @NotBlank(message = "Необходимо указать имя")
    @Pattern(regexp = "^[0-9a-zA-Zа-яА-Я]{2,255}$", message = "Длина названия компании должна быть от 2 до 20 символов.")
    String name;
    @NotBlank(message = "Необходимо указать описание")
    @Pattern(regexp = "^[0-9a-zA-Zа-яА-Я-@#$%^&+=!]{2,500}$", message = "Длина описания должна быть от 2 до 500 символов.")
    String description;
    @NotNull(message = "Необходимо выбрать страну")
    Country country;
}