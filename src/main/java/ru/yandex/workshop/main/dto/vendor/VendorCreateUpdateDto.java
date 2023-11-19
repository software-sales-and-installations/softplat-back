package ru.yandex.workshop.main.dto.vendor;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.yandex.workshop.main.dto.validation.New;
import ru.yandex.workshop.main.model.vendor.Country;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VendorCreateUpdateDto {
    @NotBlank(groups = {New.class}, message = "Необходимо указать имя")
    @Pattern(regexp = "^[a-zA-Zа-яА-Я-,.\\s]{2,255}$", message = "Длина названия компании должна быть от 2 до 20 символов.")
    String name;
    @NotBlank(groups = {New.class}, message = "Необходимо указать описание")
    @Pattern(regexp = "^[0-9a-zA-Zа-яА-Я-@#$.,%^&+=!\\s]{2,500}$", message = "Длина описания должна быть от 2 до 500 символов.")
    String description;
    @NotNull(groups = {New.class}, message = "Необходимо выбрать страну")
    Country country;
}