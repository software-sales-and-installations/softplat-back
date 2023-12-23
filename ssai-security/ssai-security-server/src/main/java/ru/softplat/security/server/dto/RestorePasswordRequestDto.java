package ru.softplat.security.server.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RestorePasswordRequestDto {
    @NotBlank(message = "Необходимо указать адрес электронной почты")
    @Pattern(regexp = "^([a-zA-Z0-9._-]){2,17}+(@[a-zA-Z]{2,8})+(\\.[a-zA-Z]{2,3})$",
            message = "Длина почты должна быть от 8 до 30 символов.")
    String email;
}
