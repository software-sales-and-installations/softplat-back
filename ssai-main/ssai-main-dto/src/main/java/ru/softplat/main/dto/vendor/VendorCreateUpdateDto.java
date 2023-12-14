package ru.softplat.main.dto.vendor;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.softplat.main.dto.validation.New;
import ru.softplat.main.dto.validation.Update;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VendorCreateUpdateDto {
    @NotBlank(groups = {New.class}, message = "Необходимо указать имя")
    @Pattern(groups = {New.class, Update.class}, regexp = "^[a-zA-Zа-яёЁА-Я-,.\"'«»\\s]{2,255}$",
            message = "Длина названия компании должна быть от 2 до 255 символов и содержать только русские " +
                    "или латинские буквы.")
    String name;
    @NotBlank(groups = {New.class}, message = "Необходимо указать описание")
    @Pattern(groups = {New.class, Update.class}, regexp = "^[0-9a-zA-Zа-яёЁА-Я-@#$.,%^&+=?!\"'«»\\s]{2,500}$",
            message = "Длина описания должна быть от 2 до 500 символов и содержать только русские " +
                    "или латинские буквы.")
    String description;
    @NotNull(groups = {New.class}, message = "Необходимо выбрать страну")
    Country country;
}