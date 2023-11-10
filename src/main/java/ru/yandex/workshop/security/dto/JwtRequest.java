package ru.yandex.workshop.security.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.Length;
import ru.yandex.workshop.main.dto.validation.New;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class JwtRequest {
    @NotBlank(message = "Необходимо указать адрес электронной почты")
    @Email(message = "Email должен быть корректным адресом электронной почты")
    @Length(min = 6, max = 30, message = "Адрес электронной почты должен содержать от 6 до 30 символов")
    String email;
    @Length(groups = {New.class}, min = 8, max = 40, message = "Пароль должен быть длиной от 8 до 40 символов")
    @NotBlank(message = "Необходимо указать пароль")
    String password;
    @NotBlank(groups = {New.class}, message = "Необходимо указать пароль повторно")
    String confirmPassword;
}