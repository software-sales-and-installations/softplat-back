package ru.yandex.workshop.security.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.yandex.workshop.main.dto.validation.New;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class JwtRequest {
    @NotBlank(message = "Необходимо указать адрес электронной почты")
    @Email(message = "Email должен быть корректным адресом электронной почты")
    String email;
    @NotNull(message = "Необходимо указать пароль")
    String password;
    @NotNull(groups = {New.class}, message = "Необходимо указать пароль повторно")
    String confirmPassword;
}